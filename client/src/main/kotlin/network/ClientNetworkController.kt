package ru.nsu.fit.isachenko.snakegame.network

import Loggable
import SnakeServer
import SnakesProto
import game.SnakeGame
import generateAckMsg
import generateJoinMsg
import generatePingMsg
import kotlinx.coroutines.*
import network.EndPoint
import ru.nsu.fit.isachenko.snakegame.ui.Painter
import java.net.DatagramPacket
import java.net.InetAddress
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.atomic.AtomicLong

class ClientNetworkController(
    private val config: SnakesProto.GameConfig,
    private val painter: Painter,
    private var endPoint: EndPoint,
    private var serverAddress: InetAddress,
    private var serverPort: Int
) : Loggable {

    private var isAlive = true
    private var myId = 0
    private var isDeputy = false

    private var deputy: SnakesProto.GamePlayer? = null

    var lastState: SnakesProto.GameState = SnakesProto.GameState.getDefaultInstance()
        private set

    private var lastMessageTime: Long = System.currentTimeMillis()

    private val messageQueue = ArrayBlockingQueue<SnakesProto.GameMessage>(100)
    private val notAckedMessages = mutableMapOf<SnakesProto.GameMessage, Long>()

    val lastSeq = AtomicLong(0)

    private lateinit var pingRoutine: Job
    private var listenRoutine: Job? = null

    fun addMessageToQueue(message: SnakesProto.GameMessage) {
        try {
            messageQueue.add(message)
        } catch (exc: Throwable) {
            logger.warning("Can't add message to queue due to ${exc.message}")
        }
    }

    /**
     * Connects to server and starts ping routine
     * @return true on success connection, false otherwise
     **/
    fun connect(): Boolean {
        val joinMsg = generateJoinMsg("Player_numba_van", lastSeq.getAndIncrement())
        val datagram = DatagramPacket(joinMsg.toByteArray(), joinMsg.serializedSize, serverAddress, serverPort)

        endPoint.send(datagram)
        try {
            val msg = endPoint.receive()
            val bytes = Arrays.copyOf(msg.data, msg.length)
            val protoAnswer = SnakesProto.GameMessage.parseFrom(bytes)
            if (protoAnswer.hasAck() && protoAnswer.hasReceiverId()) {
                myId = protoAnswer.receiverId
                pingRoutine = CoroutineScope(Dispatchers.IO).launch { pingRoutine() }
                return true
            }
        } catch (exc: Throwable) {
            logger.warning("Connection to server ${serverAddress.hostAddress} unsuccessful")
            logger.warning(exc.message)
        }

        return false
    }

    fun startListen() {
        listenRoutine = CoroutineScope(Dispatchers.IO).launch {
            listen()
        }
    }

    fun stop() {
        pingRoutine.cancel()
        listenRoutine?.cancel()
        endPoint.close()
        logger.info("ClientController has stopped")
    }

    private fun listen() {
        while (isAlive) {
            val result = runCatching {
                sendMessages()

                val message = receiveMessage()
                logger.info(message.toString())

                processMessage(message)
            }
            if (result.isFailure) {
                //server time out expired
                if (!isDeputy && deputy != null) {
                    serverAddress = InetAddress.getByName(deputy!!.ipAddress)
                    serverPort = deputy!!.port
                } else if (isDeputy) {
                    runServer()
                } else {
                    logger.info(result.exceptionOrNull()?.message)
                    logger.info("Server is dead and no info about deputy, shutting down")
                    isAlive = false
                }
            }
        }
    }

    private fun runServer() {
        val game = SnakeGame(config, lastState)
        val newPort = endPoint.port + 10
        val server = SnakeServer(config, endPoint, game, myId, newPort)
        server.start(true)

        serverAddress = InetAddress.getByName("localhost")
        serverPort = endPoint.port
        endPoint = endPoint.newEndPoint(newPort, config.nodeTimeoutMs)

        isDeputy = false
    }

    private suspend fun pingRoutine() {
        var time: Long
        while (true) {
            delay(config.pingDelayMs.toLong())
            time = System.currentTimeMillis()
            if (time - lastMessageTime >= config.pingDelayMs) {
                lastMessageTime = time
                val msg = generatePingMsg(lastSeq.getAndIncrement())
                notAckedMessages[msg] = time
                sendOneMessage(msg)
            }
        }
    }

    private fun processMessage(message: SnakesProto.GameMessage) {
        if (message.hasAck() && message.hasMsgSeq()) {
            confirmMessages(message.msgSeq)
        } else if (message.hasState()) {
            lastState = message.state.state
            if (deputy == null) {
                findDeputy(message)
            }
            painter.repaint(message.state.state, config.width, config.height)
        } else if (message.hasRoleChange()) {
            if (message.roleChange.receiverRole == SnakesProto.NodeRole.DEPUTY && message.receiverId == myId) {
                val ackMessage = generateAckMsg(lastSeq.getAndIncrement(), message.senderId, myId)
                addMessageToQueue(ackMessage)
                isDeputy = true
            }
        } else if (message.hasSteer() && isDeputy) {
            runServer()
        }
    }

    private fun findDeputy(message: SnakesProto.GameMessage) {
        for (player in message.state.state.players.playersList) {
            if (player?.role == SnakesProto.NodeRole.DEPUTY) {
                deputy = player
                break
            }
        }
    }

    private fun confirmMessages(seq: Long) {
        notAckedMessages.filter { it.key.msgSeq <= seq }
            .forEach { notAckedMessages.remove(it.key) }
    }

    private fun receiveMessage(): SnakesProto.GameMessage {
        val packet = endPoint.receive()
        val bytes = Arrays.copyOf(packet.data, packet.length)
        return SnakesProto.GameMessage.parseFrom(bytes)
    }

    private fun sendMessages() {
        val tmpCollection = mutableListOf<SnakesProto.GameMessage>()
        messageQueue.drainTo(tmpCollection)
        var time: Long
        tmpCollection.forEach { msg ->
            sendOneMessage(msg)
            time = System.currentTimeMillis()
            notAckedMessages[msg] = time
            lastMessageTime = time
        }
    }

    private fun sendOneMessage(message: SnakesProto.GameMessage) {
        val bytesMsg = message.toByteArray()
        val packet = DatagramPacket(bytesMsg, message.serializedSize, serverAddress, serverPort)
        endPoint.send(packet)
    }
}