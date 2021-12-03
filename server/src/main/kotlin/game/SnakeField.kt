package game

import SnakesProto


class SnakeField {

    private val snakes = mutableListOf<Snake>()

    private val food = mutableListOf<SnakesProto.GameState.Coord>()

    fun moveSnake(msg: SnakesProto.GameMessage) {
        if (!msg.hasSteer()) {
            return
        }

    }


    fun generateNewState() : SnakesProto.GameState {
        TODO()
    }

}