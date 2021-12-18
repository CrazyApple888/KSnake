package ru.nsu.fit.isachenko.snakegame.network

import Loggable
import SnakesProto
import generateAckMsg
import generateJoinMsg
import generatePingMsg
import kotlinx.coroutines.*
import network.EndPoint
import ru.nsu.fit.isachenko.snakegame.ui.Painter
import java.net.DatagramPacket
import java.net.InetAddress
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.atomic.AtomicLong

class ClientNetworkController(
    private val config: SnakesProto.GameConfig,
    private val painter: Painter,
    private val endPoint: EndPoint,
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
        val joinMsg = generateJoinMsg("Player_numba_van").toByteArray()
        val datagram = DatagramPacket(joinMsg, joinMsg.size, serverAddress, serverPort)

        endPoint.send(datagram)
        try {
            val msg = endPoint.receive()
            val protoAnswer = SnakesProto.GameMessage.parseFrom(msg.data)
            if (protoAnswer.hasAck() && protoAnswer.hasReceiverId()) {
                myId = protoAnswer.receiverId
                pingRoutine = CoroutineScope(Dispatchers.IO).launch { pingRoutine() }
                return true
            }
        } catch (exc: Throwable) {
            logger.warning("Connection to server ${serverAddress.hostAddress} unsuccessful")
        }

        return false
    }

    //TODO:
    //       Types of messages       |       Other
    // -RoleChangeMsg                |        RDT
    // -AckMsg                       |
    // -Server timed out             |
    // -StateMsg                     |
    fun listen() {
        //  send messages
        //  wait answer from server and process gotten message:
        //                                                      ack
        //                                                      roleChange =====> deputy  --> set deputy flag true
        //                                                      stateMsg   =====> repaint view
        //
        //  check timeouts and resend, if it requires
        //  and think about server timeout at the same time !!!
        //  server timeout expired                                         =====> deputy? --> make this server master,
        //                                                                                    start server routine
        //                                                                                    and close this
        //                                                                 =====> normal? --> send all to deputy
        while (isAlive) {
            val result = runCatching {
                sendMessages()

                val message = receiveMessage()

                processMessage(message)
            }
            if (result.isFailure) {
                //server time out expired
                //todo:
                // if !deputy --> change endPoint to deputy
                if (!isDeputy && deputy != null) {
                    serverAddress = InetAddress.getByName(deputy!!.ipAddress)
                    serverPort = deputy!!.port
                } else {
                //todo else start server
                }
            }
        }
    }

    private suspend fun pingRoutine() {
        var time: Long
        while (true) {
            delay(config.nodeTimeoutMs.toLong())
            time = System.currentTimeMillis()
            if (time - lastMessageTime >= config.pingDelayMs) {
                lastMessageTime = time
                val msg = generatePingMsg(lastSeq.incrementAndGet())
                notAckedMessages[msg] = time
                sendOneMessage(msg)
            }
        }
    }

    private fun processMessage(message: SnakesProto.GameMessage) {
        if (message.hasAck() && message.hasMsgSeq()) {
            confirmMessages(message.msgSeq)
        }
        else if (message.hasState()) {
            lastState = message.state.state
            if (deputy == null) {
                findDeputy(message)
            }
            painter.repaint(message.state.state)
        }
        else if (message.hasRoleChange()) {
            if (message.roleChange.receiverRole == SnakesProto.NodeRole.DEPUTY && message.receiverId == myId) {
                val ackMessage = generateAckMsg(lastSeq.incrementAndGet(), message.senderId, myId)
                addMessageToQueue(ackMessage)
                isDeputy = true
            }
        }
        else if (message.hasSteer() && isDeputy) {
            //сервер отвалился, стартуем сервер от себя. депутатом назначаем чела, который прислал steer,
            //заменяем енд поинт на созданный сервер
            TODO()
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
        //Maybe <= ?
        notAckedMessages.filter { it.key.msgSeq == seq }
            .forEach { notAckedMessages.remove(it.key) }
    }

    private fun receiveMessage(): SnakesProto.GameMessage {
        val packet = endPoint.receive()
        return SnakesProto.GameMessage.parseFrom(packet.data)
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
        val packet = DatagramPacket(bytesMsg, bytesMsg.size, serverAddress, serverPort)
        endPoint.send(packet)
    }

}