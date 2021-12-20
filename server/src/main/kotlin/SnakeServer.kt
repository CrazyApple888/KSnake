import com.google.protobuf.InvalidProtocolBufferException
import game.GameModel
import kotlinx.coroutines.*
import network.EndPoint
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.atomic.AtomicLong

class SnakeServer(
    private val gameConfig: SnakesProto.GameConfig,
    private val endPoint: EndPoint,
    private val gameModel: GameModel,
    private val myId: Int,
    private val myPlayerPort: Int
) : Loggable {

    init {

    }

    private var serverSocketJob: Job? = null
    private var promoterJob: Job? = null
    private val announcementMsg: SnakesProto.GameMessage get() = generateAnnouncementMsg()

    private var prevTime: Long = 0L
    private var msgSeq = AtomicLong(0)

    private var deputy: Pair<Int, Boolean>? = null

    //address to player
    private val players = mutableMapOf<String, SnakesProto.GamePlayer>()

    //id to time
    private val lastMessageTime = mutableMapOf<Int, Long>()
    private val notAckedMessages = mutableMapOf<Int, MutableList<SnakesProto.GameMessage>>()

    fun start() {
        serverSocketJob = CoroutineScope(Dispatchers.IO).launch {
            listen()
        }
        promoterJob = CoroutineScope(Dispatchers.IO).launch {
            sendPromotion()
        }
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val state = generateGameMessageWithState(gameModel.generateNextState(), msgSeq.getAndIncrement())
                try {
                    sendState(state)
                } catch (exc: Throwable) {
                    println(exc.message)
                }
                val a = state
                delay(gameConfig.stateDelayMs.toLong())
            }
        }
        logger.info("SnakeServer started")
    }

    fun stop() {
        serverSocketJob?.cancel()
        promoterJob?.cancel()
        logger.info("SnakeServer canceled")
    }

    private fun listen() {
        while (true) {
            val result = runCatching {
                /*if (checkTimeout()) {
                    val state = generateGameMessageWithState(gameModel.generateNextState(), msgSeq.getAndIncrement())
                    sendState(state)
                    prevTime = System.currentTimeMillis()
                }*/

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
                lastMessageTime[it.id] = System.currentTimeMillis()
            }
        } else if (message.hasAck()) {
            if (deputy != null && !deputy!!.second && message.senderId == deputy!!.first) {
                deputy = deputy!!.first to true
            }
            notAckedMessages[message.senderId]?.removeIf { it.msgSeq <= message.msgSeq }
            players[address]?.let {
                lastMessageTime[it.id] = System.currentTimeMillis()
            }
        } else if (message.hasPing()) {
            players[address]?.let {
                lastMessageTime[it.id] = System.currentTimeMillis()
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
            val packet = DatagramPacket(ackMsg.toByteArray(), ackMsg.serializedSize, InetAddress.getByName(address), port)
            endPoint.send(packet)
            lastMessageTime[id] = System.currentTimeMillis()
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
    }

    private fun parseGameMessage(msg: DatagramPacket): SnakesProto.GameMessage? {
        try {
            val strWithoutZeros = String(msg.data, StandardCharsets.UTF_8).trimEnd { it == 0.toChar() }
            val bytes = Arrays.copyOf(msg.data, msg.length) //strWithoutZeros.toByteArray(StandardCharsets.UTF_8)

            return SnakesProto.GameMessage.parseFrom(bytes)
        } catch (exc: InvalidProtocolBufferException) {
            logger.warning("SnakeServer got invalid protobuf packet, exception message: ${exc.message}")
        }

        return null
    }

    private fun checkTimeout() =
        System.currentTimeMillis() - prevTime >= gameConfig.stateDelayMs

    private suspend fun sendPromotion() = withContext(Dispatchers.IO) {
        //val multicastSocket = MulticastSocket(9192)
        val multicastAddress = InetAddress.getByName("239.192.0.4")
        //multicastSocket.joinGroup(multicastAddress)
        while (true) {
            val msg = announcementMsg.toByteArray()
            val packet = DatagramPacket(msg, msg.size, multicastAddress, 9192)
            endPoint.send(packet)
            delay(1000L)
        }
    }

    //todo extract it to ProtobufFactory
    private fun generateAnnouncementMsg() =
        SnakesProto.GameMessage.newBuilder()
            .setAnnouncement(
                SnakesProto.GameMessage.AnnouncementMsg.newBuilder()
                    .setConfig(gameConfig)
                    .setCanJoin(true)
                    .setPlayers(SnakesProto.GamePlayers.newBuilder().addAllPlayers(players.values).build())
                    .build()
            )
            .setMsgSeq(msgSeq.getAndIncrement())
            .setSenderId(myId)
            .build()
}