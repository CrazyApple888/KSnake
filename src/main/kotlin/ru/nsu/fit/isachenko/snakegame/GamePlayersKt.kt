//Generated by the protocol buffer compiler. DO NOT EDIT!
// source: snakes.proto

package ru.nsu.fit.isachenko.snakegame;

@kotlin.jvm.JvmSynthetic
inline fun gamePlayers(block: ru.nsu.fit.isachenko.snakegame.GamePlayersKt.Dsl.() -> Unit): ru.nsu.fit.isachenko.snakegame.SnakesProto.GamePlayers =
  ru.nsu.fit.isachenko.snakegame.GamePlayersKt.Dsl._create(ru.nsu.fit.isachenko.snakegame.SnakesProto.GamePlayers.newBuilder()).apply { block() }._build()
object GamePlayersKt {
  @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
  @com.google.protobuf.kotlin.ProtoDslMarker
  class Dsl private constructor(
    @kotlin.jvm.JvmField private val _builder: ru.nsu.fit.isachenko.snakegame.SnakesProto.GamePlayers.Builder
  ) {
    companion object {
      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _create(builder: ru.nsu.fit.isachenko.snakegame.SnakesProto.GamePlayers.Builder): Dsl = Dsl(builder)
    }

    @kotlin.jvm.JvmSynthetic
    @kotlin.PublishedApi
    internal fun _build(): ru.nsu.fit.isachenko.snakegame.SnakesProto.GamePlayers = _builder.build()

    /**
     * An uninstantiable, behaviorless type to represent the field in
     * generics.
     */
    @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
    class PlayersProxy private constructor() : com.google.protobuf.kotlin.DslProxy()
    /**
     * <pre>
     * Список всех игроков
     * </pre>
     *
     * <code>repeated .snakes.GamePlayer players = 1;</code>
     */
     val players: com.google.protobuf.kotlin.DslList<ru.nsu.fit.isachenko.snakegame.SnakesProto.GamePlayer, PlayersProxy>
      @kotlin.jvm.JvmSynthetic
      get() = com.google.protobuf.kotlin.DslList(
        _builder.getPlayersList()
      )
    /**
     * <pre>
     * Список всех игроков
     * </pre>
     *
     * <code>repeated .snakes.GamePlayer players = 1;</code>
     * @param value The players to add.
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("addPlayers")
    fun com.google.protobuf.kotlin.DslList<ru.nsu.fit.isachenko.snakegame.SnakesProto.GamePlayer, PlayersProxy>.add(value: ru.nsu.fit.isachenko.snakegame.SnakesProto.GamePlayer) {
      _builder.addPlayers(value)
    }/**
     * <pre>
     * Список всех игроков
     * </pre>
     *
     * <code>repeated .snakes.GamePlayer players = 1;</code>
     * @param value The players to add.
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("plusAssignPlayers")
    inline operator fun com.google.protobuf.kotlin.DslList<ru.nsu.fit.isachenko.snakegame.SnakesProto.GamePlayer, PlayersProxy>.plusAssign(value: ru.nsu.fit.isachenko.snakegame.SnakesProto.GamePlayer) {
      add(value)
    }/**
     * <pre>
     * Список всех игроков
     * </pre>
     *
     * <code>repeated .snakes.GamePlayer players = 1;</code>
     * @param values The players to add.
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("addAllPlayers")
    fun com.google.protobuf.kotlin.DslList<ru.nsu.fit.isachenko.snakegame.SnakesProto.GamePlayer, PlayersProxy>.addAll(values: kotlin.collections.Iterable<ru.nsu.fit.isachenko.snakegame.SnakesProto.GamePlayer>) {
      _builder.addAllPlayers(values)
    }/**
     * <pre>
     * Список всех игроков
     * </pre>
     *
     * <code>repeated .snakes.GamePlayer players = 1;</code>
     * @param values The players to add.
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("plusAssignAllPlayers")
    inline operator fun com.google.protobuf.kotlin.DslList<ru.nsu.fit.isachenko.snakegame.SnakesProto.GamePlayer, PlayersProxy>.plusAssign(values: kotlin.collections.Iterable<ru.nsu.fit.isachenko.snakegame.SnakesProto.GamePlayer>) {
      addAll(values)
    }/**
     * <pre>
     * Список всех игроков
     * </pre>
     *
     * <code>repeated .snakes.GamePlayer players = 1;</code>
     * @param index The index to set the value at.
     * @param value The players to set.
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("setPlayers")
    operator fun com.google.protobuf.kotlin.DslList<ru.nsu.fit.isachenko.snakegame.SnakesProto.GamePlayer, PlayersProxy>.set(index: kotlin.Int, value: ru.nsu.fit.isachenko.snakegame.SnakesProto.GamePlayer) {
      _builder.setPlayers(index, value)
    }/**
     * <pre>
     * Список всех игроков
     * </pre>
     *
     * <code>repeated .snakes.GamePlayer players = 1;</code>
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("clearPlayers")
    fun com.google.protobuf.kotlin.DslList<ru.nsu.fit.isachenko.snakegame.SnakesProto.GamePlayer, PlayersProxy>.clear() {
      _builder.clearPlayers()
    }}
}
@kotlin.jvm.JvmSynthetic
inline fun ru.nsu.fit.isachenko.snakegame.SnakesProto.GamePlayers.copy(block: ru.nsu.fit.isachenko.snakegame.GamePlayersKt.Dsl.() -> Unit): ru.nsu.fit.isachenko.snakegame.SnakesProto.GamePlayers =
  ru.nsu.fit.isachenko.snakegame.GamePlayersKt.Dsl._create(this.toBuilder()).apply { block() }._build()