package ru.nsu.fit.isachenko.snakegame.network

import Loggable
import SnakesProto
import generateJoinMsg
import network.EndPoint
import ru.nsu.fit.isachenko.snakegame.ui.Painter
import java.net.DatagramPacket
import java.net.InetAddress
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.atomic.AtomicLong

class ClientNetworkController(
    private val painter: Painter,
    private val endPoint: EndPoint,
    private val address: InetAddress,
    private val port: Int
) : Loggable {

    private var isAlive = true
    private var id = 0
    private var isDeputy = false
    private val messageQueue = ArrayBlockingQueue<SnakesProto.GameMessage>(100)
    private val messagesToSend = mutableMapOf<SnakesProto.GameMessage, Long>()

    val lastSeq = AtomicLong(0)

    fun addMessageToQueue(message: SnakesProto.GameMessage) {
        try {
            messageQueue.add(message)
        } catch (exc: Throwable) {
            logger.warning("Can't ass message to queue due to ${exc.message}")
        }
    }

    fun connect(): Boolean {
        val joinMsg = generateJoinMsg("Player_numba_van").toByteArray()
        val datagram = DatagramPacket(joinMsg, joinMsg.size, address, port)

        endPoint.send(datagram)
        try {
            val msg = endPoint.receive()
            val protoAnswer = SnakesProto.GameMessage.parseFrom(msg.data)
            if (protoAnswer.hasAck() && protoAnswer.hasReceiverId()) {
                id = protoAnswer.receiverId
                return true
            }
        } catch (exc: Throwable) {
            logger.warning("Connection to server ${address.hostName} unsuccessful")
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
            runCatching {
                sendMessages()

                val message = receiveMessage()

                processMessage(message)
            }
        }
    }

    private fun processMessage(message: SnakesProto.GameMessage) {
        if (message.hasRoleChange()) {
            //todo change role
        } else if (message.hasAck()) {
            confirmMessages(message.msgSeq)
        }
    }

    private fun confirmMessages(seq: Long) {
        //Maybe <= ?
        messagesToSend.filter { it.key.msgSeq == seq }
            .forEach { messagesToSend.remove(it.key) }
    }

    private fun receiveMessage(): SnakesProto.GameMessage {
        val packet = endPoint.receive()
        return SnakesProto.GameMessage.parseFrom(packet.data)
    }

    private fun sendMessages() {
        val tmpCollection = mutableListOf<SnakesProto.GameMessage>()
        messageQueue.drainTo(tmpCollection)
        tmpCollection.forEach { msg ->
            sendOneMessage(msg)
            messagesToSend[msg] = System.currentTimeMillis()
        }
    }

    private fun sendOneMessage(message: SnakesProto.GameMessage) {
        val bytesMsg = message.toByteArray()
        val packet = DatagramPacket(bytesMsg, bytesMsg.size, address, port)
        endPoint.send(packet)
    }

}