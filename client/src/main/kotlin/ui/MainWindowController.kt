package ru.nsu.fit.isachenko.snakegame.ui

import MulticastObserver
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.SplitPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import ServerDTO
import SnakeServer
import SnakesProto
import game.GameConfiguration
import game.SnakeGame
import generateSteerMsg
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import network.SocketEndPoint
import ru.nsu.fit.isachenko.snakegame.network.ClientNetworkController
import java.net.InetAddress
import java.net.URL
import java.util.*

class MainWindowController : MulticastObserver {
    @FXML
    private var resources: ResourceBundle? = null

    @FXML
    private var location: URL? = null

    @FXML
    private var splitPane: SplitPane? = null

    @FXML
    var gameCanvas: Canvas? = null
        private set

    var netController: ClientNetworkController? = null

    @FXML
    private var infoVbox: VBox? = null

    @FXML
    private var gameInfoHbox: HBox? = null

    @FXML
    private var ratingVbox: VBox? = null

    @FXML
    var ratingListview: ListView<String>? = null
        private set

    @FXML
    private var gameInfoVbox: VBox? = null

    @FXML
    private var masterText: Text? = null

    @FXML
    private var gameSizeText: Text? = null

    @FXML
    private var foodParametersText: Text? = null

    @FXML
    private var buttonsHbox: HBox? = null

    @FXML
    private var leaveButton: Button? = null

    @FXML
    private var newGameButton: Button? = null

    @FXML
    private var avaibleGamesListview: ListView<String>? = null

    private var xScale = 1.0
    private var yScale = 1.0

    @FXML
    fun initialize() {
        assert(splitPane != null) { "fx:id=\"ui_splitpane\" was not injected: check your FXML file 'main_window.fxml'." }
        assert(gameCanvas != null) { "fx:id=\"gameCanvas\" was not injected: check your FXML file 'main_window.fxml'." }
        assert(infoVbox != null) { "fx:id=\"info_vbox\" was not injected: check your FXML file 'main_window.fxml'." }
        assert(gameInfoHbox != null) { "fx:id=\"game_info_hbox\" was not injected: check your FXML file 'main_window.fxml'." }
        assert(ratingVbox != null) { "fx:id=\"rating_vbox\" was not injected: check your FXML file 'main_window.fxml'." }
        assert(ratingListview != null) { "fx:id=\"rating_listview\" was not injected: check your FXML file 'main_window.fxml'." }
        assert(gameInfoVbox != null) { "fx:id=\"game_info_vbox\" was not injected: check your FXML file 'main_window.fxml'." }
        assert(masterText != null) { "fx:id=\"master_text\" was not injected: check your FXML file 'main_window.fxml'." }
        assert(gameSizeText != null) { "fx:id=\"game_size_text\" was not injected: check your FXML file 'main_window.fxml'." }
        assert(foodParametersText != null) { "fx:id=\"food_parameters_text\" was not injected: check your FXML file 'main_window.fxml'." }
        assert(buttonsHbox != null) { "fx:id=\"buttons_hbox\" was not injected: check your FXML file 'main_window.fxml'." }
        assert(leaveButton != null) { "fx:id=\"leave_button\" was not injected: check your FXML file 'main_window.fxml'." }
        assert(newGameButton != null) { "fx:id=\"new_game_button\" was not injected: check your FXML file 'main_window.fxml'." }
        assert(avaibleGamesListview != null) { "fx:id=\"avaible_games_listview\" was not injected: check your FXML file 'main_window.fxml'." }

        avaibleGamesListview?.setOnMouseClicked {
            //todo handle clicks
        }

        newGameButton?.setOnMouseClicked {
            println("BUTTON PRESSED")
            val config = GameConfiguration.buildConfig()
            val game = SnakeGame(config)
            val serverEndPoint = SocketEndPoint(8080, config.nodeTimeoutMs)
            val clientEndPoint = SocketEndPoint(8090, config.nodeTimeoutMs)
            val server = SnakeServer(config, serverEndPoint, game, 1, 8090)
            val painter = FXPainter(gameCanvas!!, ratingListview!!)
            painter.countCanvasScale(config.width, config.height)
            val client =
                ClientNetworkController(config, painter, clientEndPoint, InetAddress.getByName("localhost"), 8080)
            server.start()
            netController = client
            client.connect()
            client.startListen()
        }
    }


    override fun notify(newServer: ServerDTO) {
        avaibleGamesListview?.items?.add("$newServer")
    }

    fun handleKey(event: KeyEvent) {
        println("Key pressed")
        val direction = when (event.code) {
            KeyCode.W -> SnakesProto.Direction.UP
            KeyCode.S -> SnakesProto.Direction.DOWN
            KeyCode.D -> SnakesProto.Direction.RIGHT
            KeyCode.A -> SnakesProto.Direction.LEFT
            else -> return
        }
        netController?.lastSeq?.getAndIncrement()?.let {
            val msg = generateSteerMsg(direction, it)
            netController?.addMessageToQueue(msg)
        }
    }
}