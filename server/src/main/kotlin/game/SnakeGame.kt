package game

import SnakesProto
import generateCoordMsg
import euclidToRing
import generateSnakeWithRandomDirection


class SnakeGame(
    private val config: SnakesProto.GameConfig
) : GameModel {

    val players = mutableListOf<SnakesProto.GamePlayer>()

    private val aliveSnakes = mutableListOf<SnakesProto.GameState.Snake>()
    private val deadSnakes = mutableListOf<SnakesProto.GameState.Snake>()
    private val food = mutableListOf<SnakesProto.GameState.Coord>()
    private var lastId = 0
    private var stateOrder = 0

    constructor(config: SnakesProto.GameConfig, initialState: SnakesProto.GameState) : this(config) {
        stateOrder = initialState.stateOrder
        initialState.snakesList.forEach { snake ->
            if (snake.state == SnakesProto.GameState.Snake.SnakeState.ALIVE) {
                aliveSnakes += snake
            } else {
                deadSnakes += snake
            }
        }
        food += initialState.foodsList
        players += initialState.players.playersList
    }

    override fun tryAddSnake(): Int? {
        var topLeftCoord = 0 to 0
        while (topLeftCoord != (config.width to config.height)) {
            val coord = generateCoordMsg(topLeftCoord.first, topLeftCoord.second)
            val isPlaceFree = isPlaceFree(coord)
            if (isPlaceFree) {
                if (isAreaFree(coord)) {
                    val snake = generateSnakeWithRandomDirection(
                        lastId++,
                        euclidToRing(coord.x, config.width),
                        euclidToRing(coord.y, config.height),
                        config
                    )
                    aliveSnakes.add(snake)
                    return snake.playerId
                } else {
                    topLeftCoord = topLeftCoord.first + 1 to topLeftCoord.second + 1
                }
            }
        }

        return null
    }

    private fun isAreaFree(coord: SnakesProto.GameState.Coord): Boolean {
        for (x in coord.x..coord.x + 4) {
            for (y in coord.y..coord.y + 4) {
                val actualCoord = euclidToRing(x, config.width) to euclidToRing(y, config.height)
                if (!isPlaceFree(generateCoordMsg(actualCoord.first, actualCoord.second))) {
                    return false
                }
            }
        }

        return true
    }

    private fun isPlaceFree(coord: SnakesProto.GameState.Coord): Boolean {
        val allSnakes = aliveSnakes + deadSnakes
        return (null == allSnakes.find { snake -> snake.pointsList.contains(coord) }) && !food.contains(coord)
    }

    private fun doGameMove() {
        aliveSnakes.replaceAll { moveSnake(it) }
    }

    override fun generateNextState(): SnakesProto.GameState {
        doGameMove()
        stateOrder++
        return SnakesProto.GameState.newBuilder()
            .setStateOrder(stateOrder)
            .addAllSnakes(aliveSnakes + deadSnakes)
            .addAllFoods(food)
            .setPlayers(
                SnakesProto.GamePlayers.newBuilder()
                    .addAllPlayers(players)
                    .build()
            )
            .setConfig(config)
            .build()
    }

    override fun changeSnakeDirection(playerId: Int, direction: SnakesProto.Direction) {
        aliveSnakes.find { it.playerId == playerId }
            ?.also {
                it.toBuilder()
                    .setHeadDirection(direction)
                    .build()
            }
    }

    private fun moveSnake(snake: SnakesProto.GameState.Snake): SnakesProto.GameState.Snake =
        when (snake.headDirection) {
            SnakesProto.Direction.UP -> {
                val points = snake.pointsList.map {
                    val y = euclidToRing(it.y - 1, config.height)
                    generateCoordMsg(it.x, y)
                }
                snake.toBuilder()
                    .clearPoints()
                    .addAllPoints(points)
                    .build()
            }
            SnakesProto.Direction.DOWN -> {
                val points = snake.pointsList.map {
                    val y = euclidToRing(it.y + 1, config.height)
                    generateCoordMsg(it.x, y)
                }
                snake.toBuilder()
                    .clearPoints()
                    .addAllPoints(points)
                    .build()
            }
            SnakesProto.Direction.LEFT -> {
                val points = snake.pointsList.map {
                    val x = euclidToRing(it.x - 1, config.width)
                    generateCoordMsg(x, it.y)
                }
                snake.toBuilder()
                    .clearPoints()
                    .addAllPoints(points)
                    .build()
            }
            SnakesProto.Direction.RIGHT -> {
                val points = snake.pointsList.map {
                    val x = euclidToRing(it.x + 1, config.width)
                    generateCoordMsg(x, it.y)
                }
                snake.toBuilder()
                    .clearPoints()
                    .addAllPoints(points)
                    .build()
            }
            else -> throw IllegalArgumentException("SnakesProto.Direction is null")
        }

}