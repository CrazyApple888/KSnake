package game

import Loggable
import java.util.*

object GameConfiguration : Loggable {

    private val config = mutableMapOf(
        "width" to 40.0,            //ширина поля
        "height" to 30.0,           //высота поля
        "food_static" to 1.0,       //множитель еды
        "food_per_player" to 1.0,   //множитель еды на игрока
        "state_delay_ms" to 1000.0, //обновлять состояние каждый этот период
        "dead_food_prob" to 0.1,    //вероятность превращения змейки в еду
        "ping_delay_ms" to 100.0,   //если ничего не шлем в течение этого, то надо отправить пинг
        "node_timeout_ms" to 80000.0, //после этого таймаута клиент умер
    )

    fun buildConfig(): SnakesProto.GameConfig =
        SnakesProto.GameConfig.newBuilder()
            .setWidth(config.getValue("width").toInt())
            .setHeight(config.getValue("height").toInt())
            .setFoodStatic(config.getValue("food_static").toInt())
            .setFoodPerPlayer(config.getValue("food_per_player").toFloat())
            .setStateDelayMs(config.getValue("state_delay_ms").toInt())
            .setDeadFoodProb(config.getValue("dead_food_prob").toFloat())
            .setPingDelayMs(config.getValue("ping_delay_ms").toInt())
            .setNodeTimeoutMs(config.getValue("node_timeout_ms").toInt())
            .build()

    fun configure() {
        GameConfiguration::class.java.getResourceAsStream("/config.properties").use { configInputStream ->
            val properties = Properties()
            properties.load(configInputStream)
            for (name in properties.stringPropertyNames()) {
                val value = properties.getProperty(name).toDouble()
                config[name] = value
            }
        }
    }
}