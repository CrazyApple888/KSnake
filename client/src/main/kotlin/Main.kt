package ru.nsu.fit.isachenko.snakegame

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class Main : Application() {

    override fun start(primaryStage: Stage?) {
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
        fun main() {
            launch(Main::class.java)
        }
    }
}