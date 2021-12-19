package ru.nsu.fit.isachenko.snakegame.ui

import Coordinate
import Snake
import javafx.application.Platform
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

    override fun repaint(state: SnakesProto.GameState, width: Int, height: Int) = Platform.runLater {
        clearCanvas(gameCanvas.graphicsContext2D)

        //Snakes
        state.snakesList.forEach { snake ->
            paintSnake(snake, gameCanvas.graphicsContext2D, width, height)
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

    private fun paintSnake(
        snake: SnakesProto.GameState.Snake,
        graphicsContext: GraphicsContext,
        width: Int,
        height: Int
    ) {
        graphicsContext.fill = generateColorByNumber(snake.playerId)
        val snakeModel = Snake(snake, width, height)
        snakeModel.body.forEach {
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