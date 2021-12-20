class Snake(
    initialState: SnakesProto.GameState.Snake,
    private val width: Int,
    private val height: Int
) {
    private val _body = mutableListOf<Coordinate>()
    val body: List<Coordinate> get() = _body
    private var _direction: Direction = Direction.UP
    val direction get() = _direction


    init {
        var prevCoord = Coordinate(initialState.pointsList[0].x, initialState.pointsList[0].y)
        _body += prevCoord
        initialState.pointsList
            .stream()
            .skip(1)
            .forEach { coord ->
                val x = euclidToRing(prevCoord.x + coord.x, width)
                val y = euclidToRing(prevCoord.y + coord.y, height)
                prevCoord = Coordinate(x, y)
                _body += prevCoord
            }
    }

    fun contains(coordinate: Coordinate): Boolean =
        _body.contains(coordinate)

    fun changeDirection(direction: Direction) {
        if (-direction == _direction) {
            return
        }
        this._direction = direction
    }

    fun move(food: Map<Coordinate, Boolean>, direction: Direction = this._direction): Coordinate? {
        val movedHead = when (direction) {
            Direction.UP -> Coordinate(_body[0].x, euclidToRing(_body[0].y - 1, height))
            Direction.DOWN -> Coordinate(_body[0].x, euclidToRing(_body[0].y + 1, height))
            Direction.LEFT -> Coordinate(euclidToRing(_body[0].x - 1, width), _body[0].y)
            Direction.RIGHT -> Coordinate(euclidToRing(_body[0].x + 1, width), euclidToRing(_body[0].y, width))
        }
        return if (food.keys.contains(movedHead)) {
            _body.add(0, movedHead)
            food.keys.first { it == movedHead }
        } else {
            _body.removeLast()
            _body.add(0, movedHead)
            null
        }
    }

}

data class Coordinate(
    var x: Int,
    var y: Int
) {
    fun toCoord(): SnakesProto.GameState.Coord =
        SnakesProto.GameState.Coord.newBuilder()
            .setX(x)
            .setY(y)
            .build()
}

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

operator fun Direction.unaryMinus(): Direction =
    when (this) {
        Direction.UP -> Direction.DOWN
        Direction.DOWN -> Direction.UP
        Direction.LEFT -> Direction.RIGHT
        Direction.RIGHT -> Direction.LEFT
    }