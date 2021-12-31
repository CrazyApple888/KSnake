data class ServerDTO(
    val address: String,
    val port: Int,
    val gameInfo: SnakesProto.GameMessage.AnnouncementMsg
) {

    override fun toString(): String {
        return "$address \t\t\t${gameInfo.config.width}x${gameInfo.config.height} \t\t\t${gameInfo.config.foodStatic}+${gameInfo.config.foodPerPlayer}x"
    }

}