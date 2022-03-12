package ru.nsu.fit.isachenko.snakegame

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
import java.net.URL
import java.util.*

class MainWindowController {
    @FXML
    private var resources: ResourceBundle? = null

    @FXML
    private var location: URL? = null

    @FXML
    private var ui_splitpane: SplitPane? = null

    @FXML
    private var gameCanvas: Canvas? = null

    @FXML
    private var info_vbox: VBox? = null

    @FXML
    private var game_info_hbox: HBox? = null

    @FXML
    private var rating_vbox: VBox? = null

    @FXML
    private var rating_listview: ListView<*>? = null

    @FXML
    private var game_info_vbox: VBox? = null

    @FXML
    private var master_text: Text? = null

    @FXML
    private var game_size_text: Text? = null

    @FXML
    private var food_parameters_text: Text? = null

    @FXML
    private var buttons_hbox: HBox? = null

    @FXML
    private var leave_button: Button? = null

    @FXML
    private var new_game_button: Button? = null

    @FXML
    private var avaible_games_listview: ListView<*>? = null
    @FXML
    fun initialize() {

        leave_button?.setOnMouseClicked { drawShapes(gameCanvas?.graphicsContext2D!!) }
        new_game_button?.setOnMouseClicked { clearCanvas(gameCanvas?.graphicsContext2D!!) }

        assert(ui_splitpane != null) { "fx:id=\"ui_splitpane\" was not injected: check your FXML file 'main_window.fxml'." }
        assert(gameCanvas != null) { "fx:id=\"gameCanvas\" was not injected: check your FXML file 'main_window.fxml'." }
        assert(info_vbox != null) { "fx:id=\"info_vbox\" was not injected: check your FXML file 'main_window.fxml'." }
        assert(game_info_hbox != null) { "fx:id=\"game_info_hbox\" was not injected: check your FXML file 'main_window.fxml'." }
        assert(rating_vbox != null) { "fx:id=\"rating_vbox\" was not injected: check your FXML file 'main_window.fxml'." }
        assert(rating_listview != null) { "fx:id=\"rating_listview\" was not injected: check your FXML file 'main_window.fxml'." }
        assert(game_info_vbox != null) { "fx:id=\"game_info_vbox\" was not injected: check your FXML file 'main_window.fxml'." }
        assert(master_text != null) { "fx:id=\"master_text\" was not injected: check your FXML file 'main_window.fxml'." }
        assert(game_size_text != null) { "fx:id=\"game_size_text\" was not injected: check your FXML file 'main_window.fxml'." }
        assert(food_parameters_text != null) { "fx:id=\"food_parameters_text\" was not injected: check your FXML file 'main_window.fxml'." }
        assert(buttons_hbox != null) { "fx:id=\"buttons_hbox\" was not injected: check your FXML file 'main_window.fxml'." }
        assert(leave_button != null) { "fx:id=\"leave_button\" was not injected: check your FXML file 'main_window.fxml'." }
        assert(new_game_button != null) { "fx:id=\"new_game_button\" was not injected: check your FXML file 'main_window.fxml'." }
        assert(avaible_games_listview != null) { "fx:id=\"avaible_games_listview\" was not injected: check your FXML file 'main_window.fxml'." }
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

}