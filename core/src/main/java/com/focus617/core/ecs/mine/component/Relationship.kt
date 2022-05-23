package com.focus617.core.ecs.mine.component

data class Relationship(
    var childrenNumber: Int = 0,
    var first: Int = -1,    // Entity.id
    var prev: Int = -1,     // Entity.id
    var next: Int = -1,     // Entity.id
    var parent: Int = -1,   // Entity.id
)
