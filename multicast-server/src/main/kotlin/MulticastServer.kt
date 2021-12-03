import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.DatagramPacket

import java.net.InetAddress
import java.net.MulticastSocket
import java.util.*

private typealias AnnouncementMessage = SnakesProto.GameMessage.AnnouncementMsg

class MulticastServer(
    private val address: InetAddress,
    port: Int
) : Loggable, MulticastObservable {

    private val subscribers = mutableListOf<MulticastObserver>()
    private val multicastSocket = MulticastSocket(port)
    private val servers = mutableMapOf<DatagramPacket, AnnouncementMessage>()

    fun getPortByHostName(hostName: String): Optional<Int> =
        servers.keys.stream()
            .filter { it.address.hostName == hostName }
            .map { it.port }
            .findFirst()

    fun getAddressByHostName(hostName: String): Optional<InetAddress> =
        servers.keys.stream()
            .filter { it.address.hostName == hostName }
            .map { it.address }
            .findFirst()

    fun run() = CoroutineScope(Dispatchers.IO).launch {
        multicastSocket.joinGroup(address)
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val message = DatagramPacket(buffer, DEFAULT_BUFFER_SIZE)
        while (true) {
            //todo delete AFK servers
//            subscribers.forEach { it.notify(ServerDTO("ABOBA", SnakesProto.GameMessage.AnnouncementMsg.getDefaultInstance())) }
//            subscribers.forEach { it.notify(ServerDTO("NOT ABOBA", SnakesProto.GameMessage.AnnouncementMsg.getDefaultInstance())) }
//            subscribers.forEach { it.notify(ServerDTO("ABOBICH", SnakesProto.GameMessage.AnnouncementMsg.getDefaultInstance())) }
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
        if (null == servers.putIfAbsent(message, gameMessage.announcement)) {
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