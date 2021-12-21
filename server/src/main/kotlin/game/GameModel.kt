package game

import Direction
import SnakesProto

interface GameModel {

    val players: MutableMap<Int, SnakesProto.GamePlayer>

    fun tryAddSnake() : Int?

    fun generateNextState() : SnakesProto.GameState

    fun changeSnakeDirection(playerId: Int, direction: Direction)

    fun killPlayer(id: Int)

}