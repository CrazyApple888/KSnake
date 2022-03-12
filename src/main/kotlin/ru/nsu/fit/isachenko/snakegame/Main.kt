package ru.nsu.fit.isachenko.snakegame

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage


class Main : Application() {

    override fun start(primaryStage: Stage?) {
//        val javaVersion = System.getProperty("java.version")
//        val javafxVersion = System.getProperty("javafx.version")
//        val l = Label("Hello, JavaFX $javafxVersion, running on Java $javaVersion.")
//        val scene = Scene(StackPane(l), 640.0, 480.0)
//        primaryStage?.scene = scene
//        primaryStage?.show()
        val loader = FXMLLoader(javaClass.classLoader.getResource("main_window.fxml"))
        primaryStage?.title = "KSnake"
        val view = loader.load<Parent>()
        val tmp = Scene(view)

        primaryStage?.scene = tmp
        primaryStage?.isResizable = false
        primaryStage?.show()

    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Main::class.java)
        }
    }
}