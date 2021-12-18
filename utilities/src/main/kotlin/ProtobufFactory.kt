fun generateJoinMsg(name: String): SnakesProto.GameMessage =
    SnakesProto.GameMessage.newBuilder()
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
        SnakesProto.Direction.UP -> SnakesProto.GameState.Coord.newBuilder().setX(euclidToRing(headX, config.width))
            .setY(euclidToRing(headY - 1, config.height)).build()
        SnakesProto.Direction.DOWN -> SnakesProto.GameState.Coord.newBuilder().setX(euclidToRing(headX, config.width))
            .setY(euclidToRing(headY + 1, config.height)).build()
        SnakesProto.Direction.LEFT -> SnakesProto.GameState.Coord.newBuilder()
            .setX(euclidToRing(headX - 1, config.width))
            .setY(euclidToRing(headY, config.height)).build()
        SnakesProto.Direction.RIGHT -> SnakesProto.GameState.Coord.newBuilder()
            .setX(euclidToRing(headX + 1, config.width))
            .setY(euclidToRing(headY, config.height)).build()
    }

    return SnakesProto.GameState.Snake.newBuilder()
        .setPlayerId(playerId)
        .addPoints(headCoord)
        .addPoints(tail)
        .setHeadDirection(direction)
        .build()
}