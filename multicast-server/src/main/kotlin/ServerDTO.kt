data class ServerDTO(
    val address: String,
    val gameInfo: SnakesProto.GameMessage.AnnouncementMsg
) {

    override fun toString(): String {
        return "$address ${gameInfo.config.width}x${gameInfo.config.height} ${gameInfo.config.foodStatic}+${gameInfo.config.foodPerPlayer}x"
    }

}