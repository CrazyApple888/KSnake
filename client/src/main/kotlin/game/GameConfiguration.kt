package ru.nsu.fit.isachenko.snakegame.game

import Loggable
import java.util.*

object GameConfiguration : Loggable {

    private val config = mutableMapOf(
        "width" to 40.0,
        "height" to 30.0,
        "food_static" to 1.0,
        "food_per_player" to 1.0,
        "state_delay_ms" to 1000.0,
        "dead_food_prob" to 0.1,
        "ping_delay_ms" to 100.0,
        "node_timeout_ms" to 800.0,
    )

    fun getParameterByName(name: String) : Double = config.getValue(name)

    fun configure() {
        GameConfiguration::class.java.getResourceAsStream("/config.properties").use { configInputStream ->
            val properties = Properties()
            properties.load(configInputStream)
            for (name in properties.stringPropertyNames()) {
                val value = properties.getProperty(name).toDouble()
                config[name] = value
                logger.info("Config parsed ${name to value}")
            }
        }
    }
}