//Generated by the protocol buffer compiler. DO NOT EDIT!
// source: snakes.proto

package ru.nsu.fit.isachenko.snakegame;

@kotlin.jvm.JvmSynthetic
inline fun gameState(block: ru.nsu.fit.isachenko.snakegame.GameStateKt.Dsl.() -> Unit): ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState =
  ru.nsu.fit.isachenko.snakegame.GameStateKt.Dsl._create(ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.newBuilder()).apply { block() }._build()
object GameStateKt {
  @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
  @com.google.protobuf.kotlin.ProtoDslMarker
  class Dsl private constructor(
    @kotlin.jvm.JvmField private val _builder: ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Builder
  ) {
    companion object {
      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _create(builder: ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Builder): Dsl = Dsl(builder)
    }

    @kotlin.jvm.JvmSynthetic
    @kotlin.PublishedApi
    internal fun _build(): ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState = _builder.build()

    /**
     * <pre>
     * Порядковый номер состояния, уникален в пределах игры, монотонно возрастает
     * </pre>
     *
     * <code>required int32 state_order = 1;</code>
     */
    var stateOrder: kotlin.Int
      @JvmName("getStateOrder")
      get() = _builder.getStateOrder()
      @JvmName("setStateOrder")
      set(value) {
        _builder.setStateOrder(value)
      }
    /**
     * <pre>
     * Порядковый номер состояния, уникален в пределах игры, монотонно возрастает
     * </pre>
     *
     * <code>required int32 state_order = 1;</code>
     */
    fun clearStateOrder() {
      _builder.clearStateOrder()
    }
    /**
     * <pre>
     * Порядковый номер состояния, уникален в пределах игры, монотонно возрастает
     * </pre>
     *
     * <code>required int32 state_order = 1;</code>
     * @return Whether the stateOrder field is set.
     */
    fun hasStateOrder(): kotlin.Boolean {
      return _builder.hasStateOrder()
    }

    /**
     * An uninstantiable, behaviorless type to represent the field in
     * generics.
     */
    @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
    class SnakesProxy private constructor() : com.google.protobuf.kotlin.DslProxy()
    /**
     * <pre>
     * Список змей
     * </pre>
     *
     * <code>repeated .snakes.GameState.Snake snakes = 2;</code>
     */
     val snakes: com.google.protobuf.kotlin.DslList<ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Snake, SnakesProxy>
      @kotlin.jvm.JvmSynthetic
      get() = com.google.protobuf.kotlin.DslList(
        _builder.getSnakesList()
      )
    /**
     * <pre>
     * Список змей
     * </pre>
     *
     * <code>repeated .snakes.GameState.Snake snakes = 2;</code>
     * @param value The snakes to add.
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("addSnakes")
    fun com.google.protobuf.kotlin.DslList<ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Snake, SnakesProxy>.add(value: ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Snake) {
      _builder.addSnakes(value)
    }/**
     * <pre>
     * Список змей
     * </pre>
     *
     * <code>repeated .snakes.GameState.Snake snakes = 2;</code>
     * @param value The snakes to add.
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("plusAssignSnakes")
    inline operator fun com.google.protobuf.kotlin.DslList<ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Snake, SnakesProxy>.plusAssign(value: ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Snake) {
      add(value)
    }/**
     * <pre>
     * Список змей
     * </pre>
     *
     * <code>repeated .snakes.GameState.Snake snakes = 2;</code>
     * @param values The snakes to add.
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("addAllSnakes")
    fun com.google.protobuf.kotlin.DslList<ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Snake, SnakesProxy>.addAll(values: kotlin.collections.Iterable<ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Snake>) {
      _builder.addAllSnakes(values)
    }/**
     * <pre>
     * Список змей
     * </pre>
     *
     * <code>repeated .snakes.GameState.Snake snakes = 2;</code>
     * @param values The snakes to add.
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("plusAssignAllSnakes")
    inline operator fun com.google.protobuf.kotlin.DslList<ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Snake, SnakesProxy>.plusAssign(values: kotlin.collections.Iterable<ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Snake>) {
      addAll(values)
    }/**
     * <pre>
     * Список змей
     * </pre>
     *
     * <code>repeated .snakes.GameState.Snake snakes = 2;</code>
     * @param index The index to set the value at.
     * @param value The snakes to set.
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("setSnakes")
    operator fun com.google.protobuf.kotlin.DslList<ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Snake, SnakesProxy>.set(index: kotlin.Int, value: ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Snake) {
      _builder.setSnakes(index, value)
    }/**
     * <pre>
     * Список змей
     * </pre>
     *
     * <code>repeated .snakes.GameState.Snake snakes = 2;</code>
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("clearSnakes")
    fun com.google.protobuf.kotlin.DslList<ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Snake, SnakesProxy>.clear() {
      _builder.clearSnakes()
    }
    /**
     * An uninstantiable, behaviorless type to represent the field in
     * generics.
     */
    @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
    class FoodsProxy private constructor() : com.google.protobuf.kotlin.DslProxy()
    /**
     * <pre>
     * Список клеток с едой
     * </pre>
     *
     * <code>repeated .snakes.GameState.Coord foods = 3;</code>
     */
     val foods: com.google.protobuf.kotlin.DslList<ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord, FoodsProxy>
      @kotlin.jvm.JvmSynthetic
      get() = com.google.protobuf.kotlin.DslList(
        _builder.getFoodsList()
      )
    /**
     * <pre>
     * Список клеток с едой
     * </pre>
     *
     * <code>repeated .snakes.GameState.Coord foods = 3;</code>
     * @param value The foods to add.
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("addFoods")
    fun com.google.protobuf.kotlin.DslList<ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord, FoodsProxy>.add(value: ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord) {
      _builder.addFoods(value)
    }/**
     * <pre>
     * Список клеток с едой
     * </pre>
     *
     * <code>repeated .snakes.GameState.Coord foods = 3;</code>
     * @param value The foods to add.
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("plusAssignFoods")
    inline operator fun com.google.protobuf.kotlin.DslList<ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord, FoodsProxy>.plusAssign(value: ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord) {
      add(value)
    }/**
     * <pre>
     * Список клеток с едой
     * </pre>
     *
     * <code>repeated .snakes.GameState.Coord foods = 3;</code>
     * @param values The foods to add.
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("addAllFoods")
    fun com.google.protobuf.kotlin.DslList<ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord, FoodsProxy>.addAll(values: kotlin.collections.Iterable<ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord>) {
      _builder.addAllFoods(values)
    }/**
     * <pre>
     * Список клеток с едой
     * </pre>
     *
     * <code>repeated .snakes.GameState.Coord foods = 3;</code>
     * @param values The foods to add.
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("plusAssignAllFoods")
    inline operator fun com.google.protobuf.kotlin.DslList<ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord, FoodsProxy>.plusAssign(values: kotlin.collections.Iterable<ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord>) {
      addAll(values)
    }/**
     * <pre>
     * Список клеток с едой
     * </pre>
     *
     * <code>repeated .snakes.GameState.Coord foods = 3;</code>
     * @param index The index to set the value at.
     * @param value The foods to set.
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("setFoods")
    operator fun com.google.protobuf.kotlin.DslList<ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord, FoodsProxy>.set(index: kotlin.Int, value: ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord) {
      _builder.setFoods(index, value)
    }/**
     * <pre>
     * Список клеток с едой
     * </pre>
     *
     * <code>repeated .snakes.GameState.Coord foods = 3;</code>
     */
    @kotlin.jvm.JvmSynthetic
    @kotlin.jvm.JvmName("clearFoods")
    fun com.google.protobuf.kotlin.DslList<ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord, FoodsProxy>.clear() {
      _builder.clearFoods()
    }
    /**
     * <pre>
     * Актуальнейший список игроков
     * </pre>
     *
     * <code>required .snakes.GamePlayers players = 4;</code>
     */
    var players: ru.nsu.fit.isachenko.snakegame.SnakesProto.GamePlayers
      @JvmName("getPlayers")
      get() = _builder.getPlayers()
      @JvmName("setPlayers")
      set(value) {
        _builder.setPlayers(value)
      }
    /**
     * <pre>
     * Актуальнейший список игроков
     * </pre>
     *
     * <code>required .snakes.GamePlayers players = 4;</code>
     */
    fun clearPlayers() {
      _builder.clearPlayers()
    }
    /**
     * <pre>
     * Актуальнейший список игроков
     * </pre>
     *
     * <code>required .snakes.GamePlayers players = 4;</code>
     * @return Whether the players field is set.
     */
    fun hasPlayers(): kotlin.Boolean {
      return _builder.hasPlayers()
    }

    /**
     * <pre>
     * Параметры игры
     * </pre>
     *
     * <code>required .snakes.GameConfig config = 5;</code>
     */
    var config: ru.nsu.fit.isachenko.snakegame.SnakesProto.GameConfig
      @JvmName("getConfig")
      get() = _builder.getConfig()
      @JvmName("setConfig")
      set(value) {
        _builder.setConfig(value)
      }
    /**
     * <pre>
     * Параметры игры
     * </pre>
     *
     * <code>required .snakes.GameConfig config = 5;</code>
     */
    fun clearConfig() {
      _builder.clearConfig()
    }
    /**
     * <pre>
     * Параметры игры
     * </pre>
     *
     * <code>required .snakes.GameConfig config = 5;</code>
     * @return Whether the config field is set.
     */
    fun hasConfig(): kotlin.Boolean {
      return _builder.hasConfig()
    }
  }
  @kotlin.jvm.JvmSynthetic
  inline fun coord(block: ru.nsu.fit.isachenko.snakegame.GameStateKt.CoordKt.Dsl.() -> Unit): ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord =
    ru.nsu.fit.isachenko.snakegame.GameStateKt.CoordKt.Dsl._create(ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord.newBuilder()).apply { block() }._build()
  object CoordKt {
    @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
    @com.google.protobuf.kotlin.ProtoDslMarker
    class Dsl private constructor(
      @kotlin.jvm.JvmField private val _builder: ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord.Builder
    ) {
      companion object {
        @kotlin.jvm.JvmSynthetic
        @kotlin.PublishedApi
        internal fun _create(builder: ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord.Builder): Dsl = Dsl(builder)
      }

      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _build(): ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord = _builder.build()

      /**
       * <pre>
       * По горизонтальной оси, положительное направление - вправо
       * </pre>
       *
       * <code>optional sint32 x = 1 [default = 0];</code>
       */
      var x: kotlin.Int
        @JvmName("getX")
        get() = _builder.getX()
        @JvmName("setX")
        set(value) {
          _builder.setX(value)
        }
      /**
       * <pre>
       * По горизонтальной оси, положительное направление - вправо
       * </pre>
       *
       * <code>optional sint32 x = 1 [default = 0];</code>
       */
      fun clearX() {
        _builder.clearX()
      }
      /**
       * <pre>
       * По горизонтальной оси, положительное направление - вправо
       * </pre>
       *
       * <code>optional sint32 x = 1 [default = 0];</code>
       * @return Whether the x field is set.
       */
      fun hasX(): kotlin.Boolean {
        return _builder.hasX()
      }

      /**
       * <pre>
       * По вертикальной оси, положительное направление - вниз
       * </pre>
       *
       * <code>optional sint32 y = 2 [default = 0];</code>
       */
      var y: kotlin.Int
        @JvmName("getY")
        get() = _builder.getY()
        @JvmName("setY")
        set(value) {
          _builder.setY(value)
        }
      /**
       * <pre>
       * По вертикальной оси, положительное направление - вниз
       * </pre>
       *
       * <code>optional sint32 y = 2 [default = 0];</code>
       */
      fun clearY() {
        _builder.clearY()
      }
      /**
       * <pre>
       * По вертикальной оси, положительное направление - вниз
       * </pre>
       *
       * <code>optional sint32 y = 2 [default = 0];</code>
       * @return Whether the y field is set.
       */
      fun hasY(): kotlin.Boolean {
        return _builder.hasY()
      }
    }
  }
  @kotlin.jvm.JvmSynthetic
  inline fun snake(block: ru.nsu.fit.isachenko.snakegame.GameStateKt.SnakeKt.Dsl.() -> Unit): ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Snake =
    ru.nsu.fit.isachenko.snakegame.GameStateKt.SnakeKt.Dsl._create(ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Snake.newBuilder()).apply { block() }._build()
  object SnakeKt {
    @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
    @com.google.protobuf.kotlin.ProtoDslMarker
    class Dsl private constructor(
      @kotlin.jvm.JvmField private val _builder: ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Snake.Builder
    ) {
      companion object {
        @kotlin.jvm.JvmSynthetic
        @kotlin.PublishedApi
        internal fun _create(builder: ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Snake.Builder): Dsl = Dsl(builder)
      }

      @kotlin.jvm.JvmSynthetic
      @kotlin.PublishedApi
      internal fun _build(): ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Snake = _builder.build()

      /**
       * <pre>
       * Идентификатор игрока-владельца змеи, см. GamePlayer.id
       * </pre>
       *
       * <code>required int32 player_id = 1;</code>
       */
      var playerId: kotlin.Int
        @JvmName("getPlayerId")
        get() = _builder.getPlayerId()
        @JvmName("setPlayerId")
        set(value) {
          _builder.setPlayerId(value)
        }
      /**
       * <pre>
       * Идентификатор игрока-владельца змеи, см. GamePlayer.id
       * </pre>
       *
       * <code>required int32 player_id = 1;</code>
       */
      fun clearPlayerId() {
        _builder.clearPlayerId()
      }
      /**
       * <pre>
       * Идентификатор игрока-владельца змеи, см. GamePlayer.id
       * </pre>
       *
       * <code>required int32 player_id = 1;</code>
       * @return Whether the playerId field is set.
       */
      fun hasPlayerId(): kotlin.Boolean {
        return _builder.hasPlayerId()
      }

      /**
       * An uninstantiable, behaviorless type to represent the field in
       * generics.
       */
      @kotlin.OptIn(com.google.protobuf.kotlin.OnlyForUseByGeneratedProtoCode::class)
      class PointsProxy private constructor() : com.google.protobuf.kotlin.DslProxy()
      /**
       * <pre>
       * Список "ключевых" точек змеи. Первая точка хранит координаты головы змеи.
       * Каждая следующая - смещение следующей "ключевой" точки относительно предыдущей,
       * в частности последняя точка хранит смещение хвоста змеи относительно предыдущей "ключевой" точки. 
       * </pre>
       *
       * <code>repeated .snakes.GameState.Coord points = 2;</code>
       */
       val points: com.google.protobuf.kotlin.DslList<ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord, PointsProxy>
        @kotlin.jvm.JvmSynthetic
        get() = com.google.protobuf.kotlin.DslList(
          _builder.getPointsList()
        )
      /**
       * <pre>
       * Список "ключевых" точек змеи. Первая точка хранит координаты головы змеи.
       * Каждая следующая - смещение следующей "ключевой" точки относительно предыдущей,
       * в частности последняя точка хранит смещение хвоста змеи относительно предыдущей "ключевой" точки. 
       * </pre>
       *
       * <code>repeated .snakes.GameState.Coord points = 2;</code>
       * @param value The points to add.
       */
      @kotlin.jvm.JvmSynthetic
      @kotlin.jvm.JvmName("addPoints")
      fun com.google.protobuf.kotlin.DslList<ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord, PointsProxy>.add(value: ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord) {
        _builder.addPoints(value)
      }/**
       * <pre>
       * Список "ключевых" точек змеи. Первая точка хранит координаты головы змеи.
       * Каждая следующая - смещение следующей "ключевой" точки относительно предыдущей,
       * в частности последняя точка хранит смещение хвоста змеи относительно предыдущей "ключевой" точки. 
       * </pre>
       *
       * <code>repeated .snakes.GameState.Coord points = 2;</code>
       * @param value The points to add.
       */
      @kotlin.jvm.JvmSynthetic
      @kotlin.jvm.JvmName("plusAssignPoints")
      inline operator fun com.google.protobuf.kotlin.DslList<ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord, PointsProxy>.plusAssign(value: ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord) {
        add(value)
      }/**
       * <pre>
       * Список "ключевых" точек змеи. Первая точка хранит координаты головы змеи.
       * Каждая следующая - смещение следующей "ключевой" точки относительно предыдущей,
       * в частности последняя точка хранит смещение хвоста змеи относительно предыдущей "ключевой" точки. 
       * </pre>
       *
       * <code>repeated .snakes.GameState.Coord points = 2;</code>
       * @param values The points to add.
       */
      @kotlin.jvm.JvmSynthetic
      @kotlin.jvm.JvmName("addAllPoints")
      fun com.google.protobuf.kotlin.DslList<ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord, PointsProxy>.addAll(values: kotlin.collections.Iterable<ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord>) {
        _builder.addAllPoints(values)
      }/**
       * <pre>
       * Список "ключевых" точек змеи. Первая точка хранит координаты головы змеи.
       * Каждая следующая - смещение следующей "ключевой" точки относительно предыдущей,
       * в частности последняя точка хранит смещение хвоста змеи относительно предыдущей "ключевой" точки. 
       * </pre>
       *
       * <code>repeated .snakes.GameState.Coord points = 2;</code>
       * @param values The points to add.
       */
      @kotlin.jvm.JvmSynthetic
      @kotlin.jvm.JvmName("plusAssignAllPoints")
      inline operator fun com.google.protobuf.kotlin.DslList<ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord, PointsProxy>.plusAssign(values: kotlin.collections.Iterable<ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord>) {
        addAll(values)
      }/**
       * <pre>
       * Список "ключевых" точек змеи. Первая точка хранит координаты головы змеи.
       * Каждая следующая - смещение следующей "ключевой" точки относительно предыдущей,
       * в частности последняя точка хранит смещение хвоста змеи относительно предыдущей "ключевой" точки. 
       * </pre>
       *
       * <code>repeated .snakes.GameState.Coord points = 2;</code>
       * @param index The index to set the value at.
       * @param value The points to set.
       */
      @kotlin.jvm.JvmSynthetic
      @kotlin.jvm.JvmName("setPoints")
      operator fun com.google.protobuf.kotlin.DslList<ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord, PointsProxy>.set(index: kotlin.Int, value: ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord) {
        _builder.setPoints(index, value)
      }/**
       * <pre>
       * Список "ключевых" точек змеи. Первая точка хранит координаты головы змеи.
       * Каждая следующая - смещение следующей "ключевой" точки относительно предыдущей,
       * в частности последняя точка хранит смещение хвоста змеи относительно предыдущей "ключевой" точки. 
       * </pre>
       *
       * <code>repeated .snakes.GameState.Coord points = 2;</code>
       */
      @kotlin.jvm.JvmSynthetic
      @kotlin.jvm.JvmName("clearPoints")
      fun com.google.protobuf.kotlin.DslList<ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord, PointsProxy>.clear() {
        _builder.clearPoints()
      }
      /**
       * <pre>
       * статус змеи в игре
       * </pre>
       *
       * <code>required .snakes.GameState.Snake.SnakeState state = 3 [default = ALIVE];</code>
       */
      var state: ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Snake.SnakeState
        @JvmName("getState")
        get() = _builder.getState()
        @JvmName("setState")
        set(value) {
          _builder.setState(value)
        }
      /**
       * <pre>
       * статус змеи в игре
       * </pre>
       *
       * <code>required .snakes.GameState.Snake.SnakeState state = 3 [default = ALIVE];</code>
       */
      fun clearState() {
        _builder.clearState()
      }
      /**
       * <pre>
       * статус змеи в игре
       * </pre>
       *
       * <code>required .snakes.GameState.Snake.SnakeState state = 3 [default = ALIVE];</code>
       * @return Whether the state field is set.
       */
      fun hasState(): kotlin.Boolean {
        return _builder.hasState()
      }

      /**
       * <pre>
       * Направление, в котором повёрнута голова змейки в текущий момент
       * </pre>
       *
       * <code>required .snakes.Direction head_direction = 4;</code>
       */
      var headDirection: ru.nsu.fit.isachenko.snakegame.SnakesProto.Direction
        @JvmName("getHeadDirection")
        get() = _builder.getHeadDirection()
        @JvmName("setHeadDirection")
        set(value) {
          _builder.setHeadDirection(value)
        }
      /**
       * <pre>
       * Направление, в котором повёрнута голова змейки в текущий момент
       * </pre>
       *
       * <code>required .snakes.Direction head_direction = 4;</code>
       */
      fun clearHeadDirection() {
        _builder.clearHeadDirection()
      }
      /**
       * <pre>
       * Направление, в котором повёрнута голова змейки в текущий момент
       * </pre>
       *
       * <code>required .snakes.Direction head_direction = 4;</code>
       * @return Whether the headDirection field is set.
       */
      fun hasHeadDirection(): kotlin.Boolean {
        return _builder.hasHeadDirection()
      }
    }
  }
}
@kotlin.jvm.JvmSynthetic
inline fun ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.copy(block: ru.nsu.fit.isachenko.snakegame.GameStateKt.Dsl.() -> Unit): ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState =
  ru.nsu.fit.isachenko.snakegame.GameStateKt.Dsl._create(this.toBuilder()).apply { block() }._build()
@kotlin.jvm.JvmSynthetic
inline fun ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord.copy(block: ru.nsu.fit.isachenko.snakegame.GameStateKt.CoordKt.Dsl.() -> Unit): ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Coord =
  ru.nsu.fit.isachenko.snakegame.GameStateKt.CoordKt.Dsl._create(this.toBuilder()).apply { block() }._build()
@kotlin.jvm.JvmSynthetic
inline fun ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Snake.copy(block: ru.nsu.fit.isachenko.snakegame.GameStateKt.SnakeKt.Dsl.() -> Unit): ru.nsu.fit.isachenko.snakegame.SnakesProto.GameState.Snake =
  ru.nsu.fit.isachenko.snakegame.GameStateKt.SnakeKt.Dsl._create(this.toBuilder()).apply { block() }._build()
