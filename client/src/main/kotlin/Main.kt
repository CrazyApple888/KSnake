package ru.nsu.fit.isachenko.snakegame

import Loggable
import MulticastServer
import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import game.GameConfiguration
import ru.nsu.fit.isachenko.snakegame.ui.MainWindowController
import java.lang.Exception
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
        primaryStage?.requestFocus()
        logger.info("Application successfully started!")
        GameConfiguration.configure()

        val controller = loader.getController<MainWindowController>()

        multicastServer.subscribe(controller)
        multicastServer.run()
    }

    companion object {
        @JvmStatic
        fun main() {
            launch(Main::class.java)
        }
    }
}