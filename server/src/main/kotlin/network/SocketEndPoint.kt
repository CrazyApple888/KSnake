package network

import java.net.DatagramPacket
import java.net.DatagramSocket

class SocketEndPoint(
    override val port: Int,
    timeout: Int
) : EndPoint {

    private var socket = DatagramSocket(port)

    init {
        socket.soTimeout = timeout
    }

    override fun send(data: DatagramPacket) {
        socket.send(data)
    }

    override fun receive(): DatagramPacket {
        val packet = DatagramPacket(ByteArray(DEFAULT_BUFFER_SIZE), DEFAULT_BUFFER_SIZE)
        socket.receive(packet)

        return packet
    }

    override fun close() {
        socket.close()
    }

    override fun newEndPoint(port: Int, timeout: Int): EndPoint =
        SocketEndPoint(port, timeout)
}