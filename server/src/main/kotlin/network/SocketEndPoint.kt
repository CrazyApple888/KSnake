package network

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class SocketEndPoint(
    port: Int,
    timeout: Int
) : EndPoint {

    private var socket = DatagramSocket(port)

    init {
        socket.soTimeout = timeout
    }

    override fun send(data: DatagramPacket) {
        socket.send(data)
    }

    override fun receive() : DatagramPacket {
        val packet = DatagramPacket(ByteArray(DEFAULT_BUFFER_SIZE), DEFAULT_BUFFER_SIZE)
        socket.receive(packet)

        return packet
    }

    override fun close() {
        socket.close()
    }

    override fun reconfigureEndPont(port: Int) {
        socket.close()
        socket = DatagramSocket(port)
    }
}