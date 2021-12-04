package network

import java.net.DatagramPacket

interface EndPoint {

    fun send(data: DatagramPacket)

    fun receive() : DatagramPacket

    fun close()

    fun reconfigureEndPont(port: Int)

}