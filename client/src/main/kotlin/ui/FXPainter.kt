package ru.nsu.fit.isachenko.snakegame.ui

import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.ListView
import javafx.scene.paint.Color
import javafx.scene.shape.ArcType
import kotlin.math.abs

class FXPainter(
    private val gameCanvas: Canvas,
    private val ratingListview: ListView<String>
) : Painter {

    private var xScale = 1.0
    private var yScale = 1.0

    override fun repaint(state: SnakesProto.GameState) {
        clearCanvas(gameCanvas.graphicsContext2D)

        //Snakes
        state.snakesList.forEach { snake ->
            paintSnake(snake, gameCanvas.graphicsContext2D)
        }

        //Food
        state.foodsList.forEach {
            paintFood(it, gameCanvas.graphicsContext2D)
        }

        //Score
        ratingListview.items?.clear()
        state.players.playersList.forEach { player ->
            ratingListview.items?.add("${player.name}: ${player.score}")
        }

        //todo delete this
        drawShapes(gameCanvas.graphicsContext2D)
    }

    //todo delete this
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
        gc.clearRect(0.0, 0.0, gameCanvas.width, gameCanvas.height)
    }

    override fun countCanvasScale(width: Int, height: Int) {
        if (width <= 0 || height <= 0) {
            throw IllegalArgumentException("Width and height must be more than 0!")
        }
        yScale = gameCanvas.height / height
        xScale = gameCanvas.width / width
    }

    private fun paintFood(food: SnakesProto.GameState.Coord, graphicsContext: GraphicsContext) {
        graphicsContext.fill = Color.RED
        graphicsContext.fillRect(food.x * xScale, food.y * yScale, xScale, yScale)
    }

    private fun paintSnake(snake: SnakesProto.GameState.Snake, graphicsContext: GraphicsContext) {
        //todo rewrite for moves
        graphicsContext.fill = generateColorByNumber(snake.playerId)
        snake.pointsList.forEach {
            graphicsContext.fillRect(it.x * xScale, it.y * yScale, xScale, yScale)
        }
    }

    private fun generateColorByNumber(number: Int): Color {
        val r = 255 % (abs(number) + 1) + 1
        val g = 255 / (abs(number) + 1)
        val b = 255 / r

        return Color.rgb(r, g, b)
    }
}