package com.focus617.core.engine.math

class Point2D(var x: Float, var y: Float) {

    fun homogeneouCoord(): Vector3 {
        return Vector3(x, y, 1F)
    }

    override fun toString(): String = "Point($x, $y)"

    override fun equals(other: Any?): Boolean =
        if (other !is Point2D) false
        else ((x == other.x) && (y == other.y))

    operator fun minus(other: Point2D) =
        Vector2(x - other.x, y - other.y)

    operator fun plus(other: Point2D): Point2D {
        val pointA = this.homogeneouCoord()
        val pointB = other.homogeneouCoord()
        return Point2D(
            (pointA.x+pointB.x)/(pointA.z+pointB.z),
            (pointA.y+pointB.y)/(pointA.z+pointB.z)
        )
    }

    operator fun plus(other: Vector2) =
        Point2D(x + other.x, y + other.y)

    fun translate(vector: Vector2) = plus(vector)

    fun isAtLeftOfVector(from: Point2D, to: Point2D): Boolean {
        val vec1 = Vector2(from, to)
        val vec2 = Vector2(from, this)
        return (vec1.crossProduct(vec2).z > 0F)
    }

    /** 判断这个点是否在三角形内部 */
    fun isInsideTriangle(triangle: Triangle2D): Boolean =
        (this.isAtSameSide(triangle.point0, triangle.point1, triangle.point2))
                && (this.isAtSameSide(triangle.point1, triangle.point2, triangle.point0))
                && (this.isAtSameSide(triangle.point2, triangle.point0, triangle.point1))

    /** 判断这个点是否在三角形某边的对侧顶点（例如AB边对侧顶点C）的同侧 */
    private fun isAtSameSide(pointA: Point2D, pointB: Point2D, pointC: Point2D): Boolean {
        val vectAB: Vector2 = pointB - pointA
        val vectAC: Vector2 = pointC - pointA
        val vectAP: Vector2 = this - pointA

        val v1 = vectAB.crossProduct(vectAC)
        val v2 = vectAB.crossProduct(vectAP)

        // v1 and v2 should in same direction if P inside triangle
        return (v1.dotProduct(v2) >= 0)
    }

}