package com.focus617.core.engine.math

/**
 * Point math utilities.
 *
 * @param x x coordinate of a point
 * @param y y coordinate of a point
 * @param z z coordinate of a point
 */
class Point3D(var x: Float, var y: Float, var z: Float) {

    operator fun minus(other: Point3D) = Vector3(
        x - other.x,
        y - other.y,
        z - other.z
    )

    fun translate(vector: Vector3): Point3D {
        x += vector.x
        y += vector.y
        z += vector.z
        return this
    }

    fun homogeneouCoord(): Vector4 {
        return Vector4(x, y, z, 1F)
    }
}

