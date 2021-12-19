import network.SocketEndPoint
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.SocketTimeoutException
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SocketEndPointTest {

    private lateinit var socket: SocketEndPoint

    @BeforeTest
    fun configure() {
        socket = SocketEndPoint(8080, 1000)
    }

    @AfterTest
    fun close() {
        socket.close()
    }

    @Test
    fun testReceive() {
        val messageString = "Hello"
        val message = messageString.toByteArray()
        val datagramPacket = DatagramPacket(message, message.size, InetAddress.getByName("localhost"), 8080)

        socket.send(datagramPacket)
        val msg = socket.receive()
        assertEquals(messageString, String(msg.data, 0, messageString.length), "Messages are not equals")
    }

    @Test(expected = SocketTimeoutException::class)
    fun testSocketTimeout() {
        socket.receive()
    }

    @Test
    fun testReconfigureEndPoint() {
        //socket.reconfigureEndPont(9090)

        val messageString = "Hello"
        val message = messageString.toByteArray()
        val datagramPacket = DatagramPacket(message, message.size, InetAddress.getByName("localhost"), 9090)

        socket.send(datagramPacket)
        val msg = socket.receive()
        assertEquals(messageString, String(msg.data, 0, messageString.length), "Messages are not equals")
    }

}