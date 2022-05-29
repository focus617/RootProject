package com.focus617.core.engine.math

/**
 * Point math utilities.
 *
 * @param x x coordinate of a point
 * @param y y coordinate of a point
 * @param z z coordinate of a point
 */
class Point3D(var x: Float, var y: Float, var z: Float) {

    fun toVector3() = Vector3(x, y, z)

    fun homogeneouCoord(): Vector4 {
        return Vector4(x, y, z, 1F)
    }

    override fun toString(): String = "Point($x, $y, $z)"

    override fun equals(other: Any?): Boolean =
        if (other !is Point3D) false
        else ((x == other.x) && (y == other.y) && (z == other.z))

    operator fun minus(other: Point3D) =
        Vector3(x - other.x, y - other.y, z - other.z)

    operator fun plus(other: Point3D): Point3D {
        val pointA = this.homogeneouCoord()
        val pointB = other.homogeneouCoord()
        return Point3D(
            (pointA.x + pointB.x) / (pointA.w + pointB.w),
            (pointA.y + pointB.y) / (pointA.w + pointB.w),
            (pointA.z + pointB.z) / (pointA.w + pointB.w)
        )
    }

    operator fun plus(other: Vector3) =
        Point3D(x + other.x, y + other.y, z + other.z)

    fun setValue(valueX: Float, valueY: Float, valueZ: Float) {
        x = valueX
        y = valueY
        z = valueZ
    }

    fun translate(vector: Vector3) = plus(vector)

}

