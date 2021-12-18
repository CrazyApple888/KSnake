package ru.nsu.fit.isachenko.snakegame

import Loggable
import MulticastServer
import SnakesProto
import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import game.GameConfiguration
import generateCoordMsg
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import network.SocketEndPoint
import ru.nsu.fit.isachenko.snakegame.ui.FXPainter
import ru.nsu.fit.isachenko.snakegame.ui.MainWindowController
import java.lang.Exception
import java.net.DatagramPacket
import java.net.InetAddress
import kotlin.jvm.Throws

class Main : Application(), Loggable {

    @Throws(Exception::class)
    override fun start(primaryStage: Stage?) {
        val loader = FXMLLoader(javaClass.classLoader.getResource("main_window.fxml"))
        primaryStage?.title = "KSnake"
        val view = loader.load<Parent>()
        val tmp = Scene(view)

        val multicastServer = MulticastServer(InetAddress.getByName("239.192.0.4"), 9192)

        primaryStage?.scene = tmp
        primaryStage?.isResizable = false
        primaryStage?.setOnCloseRequest {
            Platform.exit()
        }
        primaryStage?.show()
        logger.info("Application successfully started!")
        GameConfiguration.configure()

        val controller = loader.getController<MainWindowController>()

        multicastServer.subscribe(controller)
        multicastServer.run()

        val fake = fakeState()
        val painter = FXPainter(controller.gameCanvas!!, controller.ratingListview!!)

        painter.countCanvasScale(fake.config.width, fake.config.height)

        painter.repaint(fake)

        foo()
    }

    private fun foo() {
        val server = SocketEndPoint(8080, 1000)
        val client = SocketEndPoint(8090, 1000)

        val message = "Hello".toByteArray()
        val datagramPacket = DatagramPacket(message, message.size, InetAddress.getByName("localhost"), 8080)

        client.send(datagramPacket)
        CoroutineScope(Dispatchers.IO).launch {
            val msg = server.receive()
            println(String(msg.data) + " ${msg.port}")
        }

        val a = generateCoordMsg(10, 11)
        val b = generateCoordMsg(12, 13)
        val c = generateCoordMsg(10, 11)

        val list = listOf(a,b)
        println(list.contains(c))

    }

    companion object {
        @JvmStatic
        fun main() {
            launch(Main::class.java)
        }
    }

    private fun fakeState(): SnakesProto.GameState {
        return SnakesProto.GameState.newBuilder()
            .addFoods(
                SnakesProto.GameState.Coord.newBuilder()
                    .setX(0)
                    .setY(0)
                    .build()
            )
            .addSnakes(
                SnakesProto.GameState.Snake.newBuilder()
                    .setState(SnakesProto.GameState.Snake.SnakeState.ALIVE)
                    .setHeadDirection(SnakesProto.Direction.DOWN)
                    .setPlayerId(1)
                    .addPoints(
                        SnakesProto.GameState.Coord.newBuilder()
                            .setX(2)
                            .setY(2)
                            .build()
                    )
                    .addPoints(
                        SnakesProto.GameState.Coord.newBuilder()
                            .setX(3)
                            .setY(2)
                            .build()
                    )
                    .addPoints(
                        SnakesProto.GameState.Coord.newBuilder()
                            .setX(4)
                            .setY(2)
                            .build()
                    )
                    .build()
            )
            .addSnakes(
                SnakesProto.GameState.Snake.newBuilder()
                    .setState(SnakesProto.GameState.Snake.SnakeState.ALIVE)
                    .setHeadDirection(SnakesProto.Direction.DOWN)
                    .setPlayerId(2)
                    .addPoints(
                        SnakesProto.GameState.Coord.newBuilder()
                            .setX(2)
                            .setY(4)
                            .build()
                    )
                    .addPoints(
                        SnakesProto.GameState.Coord.newBuilder()
                            .setX(3)
                            .setY(4)
                            .build()
                    )
                    .addPoints(
                        SnakesProto.GameState.Coord.newBuilder()
                            .setX(4)
                            .setY(4)
                            .build()
                    )
                    .build()
            )
            .setStateOrder(1)
            .setPlayers(SnakesProto.GamePlayers.getDefaultInstance())
            .setConfig(
                SnakesProto.GameConfig
                    .newBuilder()
                    .setWidth(50)
                    .setHeight(50)
                    .build()
            )
            .build()
    }
}