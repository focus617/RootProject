package com.focus617.app_demo.engine.d2

data class Statistics(
    var drawCalls: Int,
    var quadCount: Int
) {
    fun getTotalVertexCount() = quadCount * 4
    fun getTotalIndexCount() = quadCount * 6

    fun resetStats() {
        drawCalls = 0
        quadCount = 0
    }
}