import com.google.protobuf.InvalidProtocolBufferException
import game.GameModel
import kotlinx.coroutines.*
import network.EndPoint
import java.net.DatagramPacket
import java.net.InetAddress
import java.util.*
import java.util.concurrent.atomic.AtomicLong

class SnakeServer(
    private val gameConfig: SnakesProto.GameConfig,
    private val endPoint: EndPoint,
    private val gameModel: GameModel,
    private val myId: Int,
    private val myPlayerPort: Int
) : Loggable {

    private var serverSocketJob: Job? = null
    private var promoterJob: Job? = null
    private var stateSenderJob: Job? = null
    private var timeoutCheckerJob: Job? = null

    private val announcementMsg: SnakesProto.GameMessage
        get() = generateAnnouncementMsg(
            gameConfig,
            players.values.toList(),
            msgSeq.getAndIncrement(),
            myId
        )

    private var msgSeq = AtomicLong(0)

    private var deputy: Pair<Int, Boolean>? = null

    //address to player
    private val players = mutableMapOf<String, SnakesProto.GamePlayer>()

    //address to time
    private val lastMessageTime = mutableMapOf<String, Long>()
    private val notAckedMessages = mutableMapOf<Int, MutableList<SnakesProto.GameMessage>>()

    init {
        gameModel.players.values.forEach {
            players[it.ipAddress] = it
        }
    }

    fun start(isFromDeputy: Boolean = false) {
        serverSocketJob = CoroutineScope(Dispatchers.IO).launch {
            listen()
        }
        if (isFromDeputy) {
            getPlayers()
        }
        promoterJob = CoroutineScope(Dispatchers.IO).launch {
            sendPromotion()
        }
        stateSenderJob = CoroutineScope(Dispatchers.IO).launch {
            sendStates()
        }
        timeoutCheckerJob = CoroutineScope(Dispatchers.IO).launch {
            checkTimeouts()
        }
        logger.info("SnakeServer started")
    }

    fun stop() {
        serverSocketJob?.cancel()
        promoterJob?.cancel()
        stateSenderJob?.cancel()
        timeoutCheckerJob?.cancel()
        endPoint.close()
        logger.info("SnakeServer canceled")
    }

    private fun listen() {
        while (true) {
            val result = runCatching {
                val packet = endPoint.receive()
                parseGameMessage(packet)?.let { protoMsg ->
                    logger.info(protoMsg.toString())
                    processMessage(protoMsg, packet.address.hostAddress, packet.port)
                }
            }
            if (result.isFailure) {
                logger.warning(result.exceptionOrNull()?.message)
            }
        }
    }

    private fun processMessage(message: SnakesProto.GameMessage, address: String, port: Int) {
        val playerId = players[address]?.id
        if (playerId != null && playerId != myId && deputy == null) {
            sendRoleChange(playerId, address, port)
        }
        if (message.hasSteer()) {
            players[address]?.let {
                gameModel.changeSnakeDirection(it.id, protoDirectionToDirection(message.steer.direction))
                sendAck(it.id, message.msgSeq, address, it.port)
                lastMessageTime[address] = System.currentTimeMillis()
            }
        } else if (message.hasAck()) {
            if (deputy != null && !deputy!!.second && message.senderId == deputy!!.first) {
                deputy = deputy!!.first to true
            }
            notAckedMessages[message.senderId]?.removeIf { it.msgSeq <= message.msgSeq }
            players[address]?.let {
                lastMessageTime[address] = System.currentTimeMillis()
            }
        } else if (message.hasPing()) {
            players[address]?.let {
                lastMessageTime[address] = System.currentTimeMillis()
                sendAck(it.id, message.msgSeq, address, it.port)
            }
        } else if (message.hasJoin()) {
            processNewPlayer(message, address, port)
        }
    }

    private fun sendRoleChange(receiverId: Int, address: String, port: Int) {
        deputy = receiverId to false
        val msg = generateRoleChangeMsg(
            SnakesProto.NodeRole.MASTER,
            SnakesProto.NodeRole.DEPUTY,
            receiverId,
            myId,
            msgSeq.getAndIncrement()
        ).also {
            notAckedMessages[receiverId]?.add(it)
        }
        val packet = DatagramPacket(msg.toByteArray(), msg.serializedSize, InetAddress.getByName(address), port)
        endPoint.send(packet)
    }

    private fun sendAck(targetId: Int, seq: Long, address: String, port: Int) {
        val msg = generateAckMsg(seq, myId, targetId)
        val packet = if (myId == targetId) {
            DatagramPacket(msg.toByteArray(), msg.serializedSize, InetAddress.getByName(address), myPlayerPort)
        } else {
            DatagramPacket(msg.toByteArray(), msg.serializedSize, InetAddress.getByName(address), port)
        }
        endPoint.send(packet)
    }

    private fun processNewPlayer(message: SnakesProto.GameMessage, address: String, port: Int) {
        gameModel.tryAddSnake()?.let { id ->
            generateNewPlayer(message.join.name, id, address, port).let { player ->
                gameModel.players[id] = player
                players[address] = player
            }
            val ackMsg = generateAckMsg(message.msgSeq, myId, id)
            val packet =
                DatagramPacket(ackMsg.toByteArray(), ackMsg.serializedSize, InetAddress.getByName(address), port)
            endPoint.send(packet)
            lastMessageTime[address] = System.currentTimeMillis()
        }
    }

    private fun sendState(state: SnakesProto.GameMessage) {
        val msg = state.toByteArray()
        players.entries.forEach { player ->
            val port = if (player.value.id == myId) myPlayerPort
            else player.value.port

            val packet = DatagramPacket(
                msg,
                state.serializedSize,
                InetAddress.getByName(player.key),
                port
            )
            endPoint.send(packet)
            notAckedMessages[player.value.id]?.add(state)
        }
        logger.info("State sent")
    }

    private fun parseGameMessage(msg: DatagramPacket): SnakesProto.GameMessage? {
        try {
            val bytes = Arrays.copyOf(msg.data, msg.length)

            return SnakesProto.GameMessage.parseFrom(bytes)
        } catch (exc: InvalidProtocolBufferException) {
            logger.warning("SnakeServer got invalid protobuf packet, exception message: ${exc.message}")
        }

        return null
    }

    private suspend fun checkTimeouts() {
        while (true) {
            lastMessageTime
                .filter { System.currentTimeMillis() - it.value > gameConfig.nodeTimeoutMs }
                .forEach { entry ->
                    players[entry.key]?.id?.also { gameModel.killPlayer(it) }
                    players.remove(entry.key)?.also { player ->
                        if (player.role == SnakesProto.NodeRole.DEPUTY) {
                            deputy = null
                        }
                    }
                    lastMessageTime.remove(entry.key)
                }
            delay(gameConfig.nodeTimeoutMs.toLong())
        }
    }

    private suspend fun sendPromotion() = withContext(Dispatchers.IO) {
        val multicastAddress = InetAddress.getByName("239.192.0.4")
        while (true) {
            val msg = announcementMsg.toByteArray()
            val packet = DatagramPacket(msg, msg.size, multicastAddress, 9192)
            endPoint.send(packet)
            delay(1000L)
        }
    }

    private suspend fun sendStates() {
        while (true) {
            val state = generateGameMessageWithState(gameModel.generateNextState(), msgSeq.getAndIncrement())
            try {
                sendState(state)
            } catch (exc: Throwable) {
                println(exc.message)
            }
            delay(gameConfig.stateDelayMs.toLong())
        }
    }

    private fun getPlayers() {
        players.forEach { entry ->
            val msg = generateRoleChangeMsg(
                SnakesProto.NodeRole.MASTER,
                SnakesProto.NodeRole.NORMAL,
                entry.value.id,
                myId,
                msgSeq.getAndIncrement()
            )
            val address = InetAddress.getByName(entry.key)
            val packet = DatagramPacket(msg.toByteArray(), msg.serializedSize, address, entry.value.port)
            endPoint.send(packet)
        }
    }
}