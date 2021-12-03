fun generateJoinMsg(name: String): SnakesProto.GameMessage =
    SnakesProto.GameMessage.newBuilder()
        .setJoin(
            SnakesProto.GameMessage.JoinMsg.newBuilder()
                .setName(name)
                .build()
        )
        .build()