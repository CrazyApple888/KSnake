package game

import java.util.*

class UUIDIdGenerator : IdGenerator {

    override fun generate(): String =
        UUID.randomUUID().toString()
}