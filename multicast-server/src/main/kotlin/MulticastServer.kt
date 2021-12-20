import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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

    private var listenJob: Job? = null

    fun getPortByHostAddress(hostAddress: String): Optional<Int> =
        servers.keys.stream()
            .filter { it.address.hostAddress == hostAddress }
            .map { it.port }
            .findFirst()

    fun getAddressByHostAddress(hostAddress: String): Optional<InetAddress> =
        servers.keys.stream()
            .filter { it.address.hostAddress == hostAddress }
            .map { it.address }
            .findFirst()

    fun run() {
        listenJob = CoroutineScope(Dispatchers.IO).launch {
            listen()
        }
    }

    fun stop() {
        listenJob?.cancel()
    }

    private fun listen() {
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
        if (null == servers.putIfAbsent(message, gameMessage.announcement)) {
            logger.info("Multicast spotted new server with hostAddress ${message.address.hostAddress}")
            subscribers.forEach { it.notify(ServerDTO(message.address.hostAddress, message.port, gameMessage.announcement)) }
        }
    }

    private fun parseMessage(message: DatagramPacket): SnakesProto.GameMessage? {
        return try {
            val bytes = Arrays.copyOf(message.data, message.length)
            SnakesProto.GameMessage.parseFrom(bytes)
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