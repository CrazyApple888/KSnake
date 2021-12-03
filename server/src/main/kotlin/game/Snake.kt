package game

import SnakesProto

class Snake(
    initialHeadPosition: SnakesProto.GameState.Coord,
    initialBodyPosition: List<SnakesProto.GameState.Coord>
) {

    private var _head = initialHeadPosition
    val head: SnakesProto.GameState.Coord get() = _head

    private var _body: List<SnakesProto.GameState.Coord> = initialBodyPosition
    val body: List<SnakesProto.GameState.Coord> get() = _body

    fun updateState(head: SnakesProto.GameState.Coord, body: List<SnakesProto.GameState.Coord>) {
        this._head = head
        this._body = body
    }

}