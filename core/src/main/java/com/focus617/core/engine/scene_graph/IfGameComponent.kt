package com.focus617.core.engine.scene_graph

interface IfGameComponent {
    fun input(transform: Transform)
    fun update(transform: Transform)
    fun render(transform: Transform)
}