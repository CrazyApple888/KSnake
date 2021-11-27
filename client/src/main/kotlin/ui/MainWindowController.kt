package ru.nsu.fit.isachenko.snakegame.ui

import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.SplitPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.ArcType
import javafx.scene.text.Text
import ru.nsu.fit.isachenko.snakegame.ui.BasicController
import java.net.URL
import java.util.*

class MainWindowController : BasicController {
    @FXML
    private var resources: ResourceBundle? = null

    @FXML
    private var location: URL? = null

    @FXML
    private var splitPane: SplitPane? = null

    @FXML
    private var gameCanvas: Canvas? = null

    @FXML
    private var infoVbox: VBox? = null

    @FXML
    private var gameInfoHbox: HBox? = null

    @FXML
    private var ratingVbox: VBox? = null

    @FXML
    private var ratingListview: ListView<*>? = null

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
    private var avaibleGamesListview: ListView<*>? = null
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

        leaveButton?.setOnMouseClicked { drawShapes(gameCanvas?.graphicsContext2D!!) }
        newGameButton?.setOnMouseClicked { clearCanvas(gameCanvas?.graphicsContext2D!!) }
    }

    private fun drawShapes(gc: GraphicsContext) {
        gc.fill = Color.GREEN
        gc.stroke = Color.BLUE
        gc.lineWidth = 5.0
        gc.strokeLine(40.0, 10.0, 10.0, 40.0)
        gc.fillOval(10.0, 60.0, 30.0, 30.0)
        gc.strokeOval(60.0, 60.0, 30.0, 30.0)
        gc.fillRoundRect(110.0, 60.0, 30.0, 30.0, 10.0, 10.0)
        gc.strokeRoundRect(160.0, 60.0, 30.0, 30.0, 10.0, 10.0)
        gc.fillArc(10.0, 110.0, 30.0, 30.0, 45.0, 240.0, ArcType.OPEN)
        gc.fillArc(60.0, 110.0, 30.0, 30.0, 45.0, 240.0, ArcType.CHORD)
        gc.fillArc(110.0, 110.0, 30.0, 30.0, 45.0, 240.0, ArcType.ROUND)
        gc.strokeArc(10.0, 160.0, 30.0, 30.0, 45.0, 240.0, ArcType.OPEN)
        gc.strokeArc(60.0, 160.0, 30.0, 30.0, 45.0, 240.0, ArcType.CHORD)
        gc.strokeArc(110.0, 160.0, 30.0, 30.0, 45.0, 240.0, ArcType.ROUND)
        gc.fillPolygon(doubleArrayOf(10.0, 40.0, 10.0, 40.0), doubleArrayOf(210.0, 210.0, 240.0, 240.0), 4)
        gc.strokePolygon(doubleArrayOf(60.0, 90.0, 60.0, 90.0), doubleArrayOf(210.0, 210.0, 240.0, 240.0), 4)
        gc.strokePolyline(doubleArrayOf(110.0, 140.0, 110.0, 140.0), doubleArrayOf(210.0, 210.0, 240.0, 240.0), 4)
    }

    private fun clearCanvas(gc: GraphicsContext) {
        gc.clearRect(0.0, 0.0, gameCanvas?.width!!, gameCanvas?.height!!)
    }

    override fun repaint() {
        TODO("Not yet implemented")
    }

}