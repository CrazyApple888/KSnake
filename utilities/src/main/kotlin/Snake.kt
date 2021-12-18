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
        val prevCoord = Coordinate(initialState.pointsList[0].x, initialState.pointsList[0].y)
        _body += Coordinate(prevCoord.x, prevCoord.y)
        initialState.pointsList
            .stream()
            .skip(1)
            .forEach { coord ->
                prevCoord.x = euclidToRing(prevCoord.x + coord.x, width)
                prevCoord.y = euclidToRing(prevCoord.y + coord.y, height)
                _body += prevCoord
            }
    }

    fun contains(coordinate: Coordinate): Boolean =
        _body.contains(coordinate)

    fun changeDirection(direction: Direction) {
        this._direction = direction
    }

    fun move(food: MutableMap<Coordinate, Boolean>, direction: Direction = this._direction): Boolean {
        val movedHead = when (direction) {
            Direction.UP -> Coordinate(_body[0].x, euclidToRing(_body[0].y - 1, height))
            Direction.DOWN -> Coordinate(_body[0].x, euclidToRing(_body[0].y + 1, height))
            Direction.LEFT -> Coordinate(_body[0].x - 1, euclidToRing(_body[0].y, width))
            Direction.RIGHT -> Coordinate(_body[0].x + 1, euclidToRing(_body[0].y, width))
        }
        return if (food.keys.contains(movedHead)) {
            _body.add(0, movedHead)
            food[movedHead] = true
            true
        } else {
            _body.removeLast()
            _body.add(0, movedHead)
            false
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