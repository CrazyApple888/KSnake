import java.lang.IllegalStateException

fun generateJoinMsg(name: String, seq: Long): SnakesProto.GameMessage =
    SnakesProto.GameMessage.newBuilder()
        .setMsgSeq(seq)
        .setJoin(
            SnakesProto.GameMessage.JoinMsg.newBuilder()
                .setName(name)
                .build()
        )
        .build()

fun generatePingMsg(seq: Long): SnakesProto.GameMessage =
    SnakesProto.GameMessage.newBuilder()
        .setPing(
            SnakesProto.GameMessage.PingMsg.newBuilder()
                .build()
        )
        .setMsgSeq(seq)
        .build()

fun generateAckMsg(seq: Long, senderId: Int, receiverId: Int): SnakesProto.GameMessage =
    SnakesProto.GameMessage.newBuilder()
        .setAck(
            SnakesProto.GameMessage.AckMsg.newBuilder()
                .build()
        )
        .setReceiverId(receiverId)
        .setSenderId(senderId)
        .setMsgSeq(seq)
        .build()

fun generateCoordMsg(x: Int, y: Int): SnakesProto.GameState.Coord =
    SnakesProto.GameState.Coord.newBuilder()
        .setX(x)
        .setY(y)
        .build()

fun generateSnakeWithRandomDirection(
    playerId: Int,
    headX: Int,
    headY: Int,
    config: SnakesProto.GameConfig
): SnakesProto.GameState.Snake {
    val headCoord = SnakesProto.GameState.Coord.newBuilder()
        .setX(headX)
        .setY(headY)
        .build()
    val direction = SnakesProto.Direction.values().random()
    val tail = when (direction) {
        SnakesProto.Direction.UP -> SnakesProto.GameState.Coord.newBuilder().setX(0).setY(1).build()
        SnakesProto.Direction.DOWN -> SnakesProto.GameState.Coord.newBuilder().setX(0).setY(-1).build()
        SnakesProto.Direction.LEFT -> SnakesProto.GameState.Coord.newBuilder().setX(1).setY(0).build()
        SnakesProto.Direction.RIGHT -> SnakesProto.GameState.Coord.newBuilder().setX(-1).setY(0).build()
    }

    return SnakesProto.GameState.Snake.newBuilder()
        .setPlayerId(playerId)
        .addPoints(headCoord)
        .addPoints(tail)
        .setHeadDirection(direction)
        .setState(SnakesProto.GameState.Snake.SnakeState.ALIVE)
        .build()
}

fun generateProtoSnake(snake: Snake, playerId: Int, isAlive: Boolean): SnakesProto.GameState.Snake {
    val body = snake.body
    var prev = body[0]
    val protoBody = mutableListOf<SnakesProto.GameState.Coord>()
    protoBody += generateCoordMsg(prev.x, prev.y)
    body.stream()
        .skip(1)
        .forEach { current ->
            protoBody += coordinateToProtoCoord(prev, current)
            prev = current
        }
    val state =
        if (isAlive) SnakesProto.GameState.Snake.SnakeState.ALIVE else SnakesProto.GameState.Snake.SnakeState.ZOMBIE
    return SnakesProto.GameState.Snake.newBuilder()
        .setPlayerId(playerId)
        .addAllPoints(protoBody)
        .setState(state)
        .setHeadDirection(generateProtoDirection(snake.direction))
        .build()
}

fun generateProtoDirection(direction: Direction): SnakesProto.Direction =
    when (direction) {
        Direction.UP -> SnakesProto.Direction.UP
        Direction.DOWN -> SnakesProto.Direction.DOWN
        Direction.LEFT -> SnakesProto.Direction.LEFT
        Direction.RIGHT -> SnakesProto.Direction.RIGHT
    }

fun protoDirectionToDirection(direction: SnakesProto.Direction): Direction =
    when (direction) {
        SnakesProto.Direction.UP -> Direction.UP
        SnakesProto.Direction.DOWN -> Direction.DOWN
        SnakesProto.Direction.LEFT -> Direction.LEFT
        SnakesProto.Direction.RIGHT -> Direction.RIGHT
    }

fun generateGameMessageWithState(state: SnakesProto.GameState, seq: Long): SnakesProto.GameMessage =
    SnakesProto.GameMessage.newBuilder()
        .setState(SnakesProto.GameMessage.StateMsg.newBuilder().setState(state).build())
        .setMsgSeq(seq)
        .build()


private fun coordinateToProtoCoord(prev: Coordinate, current: Coordinate): SnakesProto.GameState.Coord {
    val dx = current.x - prev.x
    val dy = current.y - prev.y
    return if (dx < 0) {
        generateCoordMsg(1, 0)
    } else if (dx > 0) {
        generateCoordMsg(-1, 0)
    } else if (dy < 0) {
        generateCoordMsg(0, 1)
    } else if (dy > 0) {
        generateCoordMsg(0, -1)
    } else {
        throw IllegalStateException("Prev and current point similar")
    }
}

fun generateNewPlayer(name: String, id: Int, address: String, port: Int): SnakesProto.GamePlayer =
    SnakesProto.GamePlayer.newBuilder()
        .setName(name)
        .setId(id)
        .setIpAddress(address)
        .setPort(port)
        .setRole(SnakesProto.NodeRole.NORMAL)
        .setScore(0)
        .build()

fun generateRoleChangeMsg(
    senderRole: SnakesProto.NodeRole,
    receiverRole: SnakesProto.NodeRole,
    receiverId: Int,
    senderId: Int,
    seq: Long
): SnakesProto.GameMessage =
    SnakesProto.GameMessage.newBuilder()
        .setRoleChange(
            SnakesProto.GameMessage.RoleChangeMsg.newBuilder()
                .setSenderRole(senderRole)
                .setReceiverRole(receiverRole)
                .build()
        )
        .setMsgSeq(seq)
        .setReceiverId(receiverId)
        .setSenderId(senderId)
        .build()

fun generateSteerMsg(direction: SnakesProto.Direction, seq: Long): SnakesProto.GameMessage =
    SnakesProto.GameMessage.newBuilder()
        .setSteer(
            SnakesProto.GameMessage.SteerMsg.newBuilder()
                .setDirection(direction)
                .build()
        )
        .setMsgSeq(seq)
        .build()