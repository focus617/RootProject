package com.focus617.core.engine.ecs.mine.component

enum class Direction(var direction: Int){
    UP(1),
    DOWN(2),
    LEFT(3),
    RIGHT(4)
}

data class Velocity(
    var speed: Float,
    var direction: Direction
)
