package ru.nsu.fit.isachenko.snakegame.ui


interface Painter {

    fun repaint(state: SnakesProto.GameState)

    fun countCanvasScale(width: Int, height: Int)

}