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