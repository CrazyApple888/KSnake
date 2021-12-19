package game

import Coordinate
import Direction
import Snake
import SnakesProto
import euclidToRing
import generateProtoSnake
import generateSnakeWithRandomDirection
import toCoordinate
import kotlin.random.Random


class SnakeGame(
    private val config: SnakesProto.GameConfig
) : GameModel {

    override val players = mutableMapOf<Int, SnakesProto.GamePlayer>()
    private val aliveSnakes = mutableMapOf<Int, Snake>()
    private val deadSnakes = mutableMapOf<Int, Snake>()
    private val food = mutableMapOf<Coordinate, Boolean>()
    private var lastId = 0
    private var stateOrder = 0

    constructor(config: SnakesProto.GameConfig, initialState: SnakesProto.GameState) : this(config) {
        stateOrder = initialState.stateOrder
        initialState.snakesList.forEach { snake ->
            if (snake.state == SnakesProto.GameState.Snake.SnakeState.ALIVE) {
                aliveSnakes += snake.playerId to Snake(snake, config.width, config.height)
            } else {
                deadSnakes += snake.playerId to Snake(snake, config.width, config.height)
            }
        }
        food += initialState.foodsList.associate { it.toCoordinate() to false }
        players += initialState.players.playersList.associateBy { player -> player.id }
    }

    override fun tryAddSnake(): Int? {
        var topLeftCoord = 0 to 0
        while (topLeftCoord != (config.width to config.height)) {
            val coord = Coordinate(topLeftCoord.first, topLeftCoord.second)
            val isPlaceFree = isPlaceFree(coord)
            if (isPlaceFree) {
                if (isAreaFree(coord)) {
                    val snake = generateSnakeWithRandomDirection(
                        ++lastId,
                        euclidToRing(coord.x, config.width),
                        euclidToRing(coord.y, config.height),
                        config
                    )
                    aliveSnakes[lastId] = Snake(snake, config.width, config.height)
                    return snake.playerId
                }
            }
            topLeftCoord = topLeftCoord.first + 1 to topLeftCoord.second + 1
        }

        return null
    }

    private fun isAreaFree(coord: Coordinate): Boolean {
        for (x in coord.x..coord.x + 4) {
            for (y in coord.y..coord.y + 4) {
                val actualCoord = euclidToRing(x, config.width) to euclidToRing(y, config.height)
                if (!isPlaceFree(Coordinate(actualCoord.first, actualCoord.second))) {
                    return false
                }
            }
        }

        return true
    }

    private fun isPlaceFree(coord: Coordinate): Boolean {
        val allSnakes = aliveSnakes + deadSnakes
        return (null == allSnakes.values.find { snake -> snake.contains(coord) }) && !food.contains(coord)
    }

    private fun addFood() {
        var foodRemaining = config.foodStatic + config.foodPerPlayer.toInt() * aliveSnakes.size - food.size
        if (foodRemaining <= 0) {
            return
        }
        val foodCoordinate = Coordinate(0, 0)
        for (x in 0..config.width) {
            foodCoordinate.x++
            for (y in 0..config.height) {
                foodCoordinate.y++
                if (isPlaceFree(foodCoordinate)) {
                    food[foodCoordinate] = false
                    if (--foodRemaining == 0) {
                        return
                    }
                }
            }
        }
    }

    private fun doGameMove() {
        //moving snakes
        aliveSnakes.forEach {
            val player = players[it.key]
            val consumedFood = it.value.move(food)
            if (consumedFood != null) {
                food[consumedFood] = true
                players[it.key] =
                    SnakesProto.GamePlayer.newBuilder(players[it.key]).setScore(player!!.score + 1).build()
            }
        }
        deadSnakes.forEach { it.value.move(food) }

        //removing consumed food
        food.entries.removeIf { it.value }

        //checking collisions
        val dead = aliveSnakes.filter { isCollision(it.value.body[0]) }
        dead.forEach { deadSnake ->
            aliveSnakes.remove(deadSnake.key)
            //creating food from dead snake
            if (Random(System.currentTimeMillis()).nextDouble(0.0, 1.0) > config.deadFoodProb) {
                deadSnake.value.body.forEach {
                    food[it] = false
                }
            }
        }

        addFood()
    }

    private fun isCollision(point: Coordinate): Boolean {
        var collisionSnake = runCatching {
            aliveSnakes.entries.first { it.value.body.contains(point) }
        }
        if (collisionSnake.isSuccess) {
            return true
        }
        collisionSnake = runCatching {
            deadSnakes.entries.first { it.value.body.contains(point) }
        }

        return collisionSnake.isSuccess
    }

    override fun generateNextState(): SnakesProto.GameState {
        doGameMove()
        stateOrder++

        return SnakesProto.GameState.newBuilder()
            .setStateOrder(stateOrder)
            .addAllSnakes(aliveSnakes.map { generateProtoSnake(it.value, it.key, true) } + deadSnakes.map {
                generateProtoSnake(
                    it.value,
                    it.key,
                    false
                )
            })
            .addAllFoods(food.keys.map { it.toCoord() })
            .setPlayers(
                SnakesProto.GamePlayers.newBuilder()
                    .addAllPlayers(players.values)
                    .build()
            )
            .setConfig(config)
            .build()
    }

    override fun changeSnakeDirection(playerId: Int, direction: Direction) {
        aliveSnakes[playerId]?.changeDirection(direction)
    }
}