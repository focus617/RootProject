package com.focus617.core.engine.math

class Vector2(var x: Float, var y: Float) {
    constructor(from: Point2D, to: Point2D) : this(to.x - from.x, to.y - from.y)

    override fun toString(): String = "($x, $y)"

    override fun equals(other: Any?): Boolean =
        if (other !is Vector2) false
        else ((x == other.x) && (y == other.y))

    //求向量的模的方法
    fun length(): Float = kotlin.math.sqrt((x * x + y * y).toDouble()).toFloat()

    //向量规格化的方法
    fun normalize(): Vector2 = scale(1f / length())

    operator fun plus(other: Vector2) = Vector2(
        x + other.x,
        y + other.y,
    )

    operator fun minus(other: Vector2) = Vector2(
        x - other.x,
        y - other.y,
    )

    fun scale(f: Float) = Vector2(
        x * f,
        y * f,
    )

    //求向量叉积的方法
    // http://en.wikipedia.org/wiki/Cross_product
    fun crossProduct(other: Vector2) = Vector3(0F, 0F, x * other.y - y * other.x)

    //求向量点积的方法
    // http://en.wikipedia.org/wiki/Dot_product
    fun dotProduct(other: Vector2): Float = x * other.x + y * other.y

    fun atOppositeDir(other: Vector2): Boolean = (this.dotProduct(other) < 0)

    companion object {

        //计算当前向量v1与参考向量v2的夹角
        fun angleBetween(vector: Vector2, ref: Vector2): Float {
            // 判断异常
            if ((vector.length() == 0F) || (ref.length() == 0F)) throw Exception()

            val cosine: Float = (vector.dotProduct(ref)) / (vector.length() * ref.length())
            val angle: Double = (kotlin.math.acos(cosine) * 180) / Math.PI
            return angle.toFloat()
        }
    }

}