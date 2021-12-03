import com.google.protobuf.InvalidProtocolBufferException
import game.GameConfiguration
import game.GameModel
import game.IdGenerator
import kotlinx.coroutines.*
import network.EndPoint
import java.net.DatagramPacket
import java.net.InetAddress
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue

class SnakeServer(
    private val endPoint: EndPoint,
    private val stateDelayMs: Long,
    private val idGenerator: IdGenerator,
    private val gameModel: GameModel
) : Loggable {
    private val serverSocketJob = CoroutineScope(Dispatchers.IO).launch {
        listen()
    }
    private val promoterJob = CoroutineScope(Dispatchers.IO).launch {
        sendPromotion()
    }
    private val gameConfig = GameConfiguration.buildConfig()
    private val announcementMsg: SnakesProto.GameMessage.AnnouncementMsg get() = generateAnnouncementMsg()

    private var prevTime: Long = 0L

    private val players = mutableMapOf<InetAddress, String>()
    private val messages = mutableMapOf<InetAddress, SnakesProto.GameMessage>()

    fun start() {
        serverSocketJob.start()
        promoterJob.start()
        logger.info("SnakeServer started")
    }

    fun cancel() {
        serverSocketJob.cancel()
        promoterJob.cancel()
        logger.info("SnakeServer canceled")
    }

    private fun listen() {
        while (true) {
            if (checkTimeout()) {
                //todo: send new state to each server
                prevTime = System.currentTimeMillis()
            }

            val packet = endPoint.receive()
            //If msg doesn't contain protobuf, continue
            val protoMsg = parseGameMessage(packet) ?: continue


        }
    }

    private fun processMessage(message: SnakesProto.GameMessage) {

        if (message.hasJoin()) {
            //todo try to accept
        }
        else if (message.hasPing()) {
            //todo process ping msg
        }
        else if (message.hasSteer()) {
            //todo move snake for this player
        }
    }

    private fun processNewPlayer() {
        if (gameModel.hasPlace()) {
            //todo send ack with id
        } else {
            //todo send error msg
        }
    }

    private fun sendState(state: SnakesProto.GameMessage) {
        //todo send new state to all clients
    }

    private fun parseGameMessage(msg: DatagramPacket) =
        try {
            SnakesProto.GameMessage.parseFrom(msg.data)
        } catch (exc: InvalidProtocolBufferException) {
            logger.warning("SnakeServer got invalid protobuf packet, exception message: ${exc.message}")
            null
        }

    private fun checkTimeout() =
        System.currentTimeMillis() - prevTime >= stateDelayMs

    private suspend fun sendPromotion() {
        while (true) {

            val msg = announcementMsg.toByteArray()
            val packet = DatagramPacket(msg, msg.size)

            endPoint.send(packet)
            delay(1000L)
        }
    }

    private fun generateAnnouncementMsg() =
        SnakesProto.GameMessage.newBuilder()
            .announcementBuilder
            .setConfig(gameConfig)
            .setCanJoin(true)
            //todo put players
            .setPlayers(SnakesProto.GamePlayers.getDefaultInstance())
            .build()
}