import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.DatagramPacket

import java.net.InetAddress
import java.net.MulticastSocket

private typealias AnnouncementMessage = SnakesProto.GameMessage.AnnouncementMsg

class MulticastServer(
    private val address: InetAddress,
    port: Int
) : Loggable, MulticastObservable {

    private val subscribers = mutableListOf<MulticastObserver>()
    private val multicastSocket = MulticastSocket(port)
    private val servers = mutableMapOf<String, AnnouncementMessage>()

    fun run() = CoroutineScope(Dispatchers.IO).launch {
        multicastSocket.joinGroup(address)
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val message = DatagramPacket(buffer, DEFAULT_BUFFER_SIZE)
        while (true) {
            //todo delete AFK servers
            multicastSocket.receive(message)
            processMessage(message)
        }
    }

    private fun processMessage(message: DatagramPacket) {
        val gameMessage = parseMessage(message)
        if (null == gameMessage || !gameMessage.hasAnnouncement()) {
            logger.warning("Multicast got message without announcement")
            return
        }
        if (null == servers.putIfAbsent(message.address.hostName, gameMessage.announcement)) {
            logger.info("Multicast spotted new server with hostName ${message.address.hostName}")
            subscribers.forEach { it.notify(ServerDTO(message.address.hostName, gameMessage.announcement)) }
        }
    }

    private fun parseMessage(message: DatagramPacket): SnakesProto.GameMessage? {
        return try {
            SnakesProto.GameMessage.parseFrom(message.data.inputStream())
        } catch (exc: Exception) {
            null
        }
    }

    override fun subscribe(subscriber: MulticastObserver) {
        subscribers.add(subscriber)
    }

    override fun unsubscribe(subscriber: MulticastObserver) {
        subscribers.remove(subscriber)
    }
}