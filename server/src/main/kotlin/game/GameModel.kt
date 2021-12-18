package game

import Direction
import SnakesProto

interface GameModel {

    fun tryAddSnake() : Int?

    fun generateNextState() : SnakesProto.GameState

    fun changeSnakeDirection(playerId: Int, direction: Direction)

}