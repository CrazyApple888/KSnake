package ru.nsu.fit.isachenko.snakegame.ui


interface Painter {

    fun repaint(state: SnakesProto.GameState, width: Int, height: Int)

    fun countCanvasScale(width: Int, height: Int)

}