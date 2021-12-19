package network

import java.net.DatagramPacket
import java.net.InetAddress

interface EndPoint {

    val port: Int
    val address: InetAddress

    fun send(data: DatagramPacket)

    fun receive() : DatagramPacket

    fun close()

}