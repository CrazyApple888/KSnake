package network

import java.net.DatagramPacket

interface EndPointFactory {
    fun newEndPoint(port: Int, timeout: Int): EndPoint
}

interface EndPoint : EndPointFactory {

    val port: Int

    fun send(data: DatagramPacket)

    fun receive(): DatagramPacket

    fun close()

}