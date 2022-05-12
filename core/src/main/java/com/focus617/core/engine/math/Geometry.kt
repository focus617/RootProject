package com.focus617.core.engine.math

import com.focus617.core.platform.base.BaseEntity


// 二维
class Triangle2D(val point0: Point2D, val point1: Point2D, val point2: Point2D)

class Circle(val center: Point3D, val radius: Float) {

    fun scale(scale: Float): Circle {
        return Circle(center, radius * scale)
    }
}

// 三维
class Ray(val point: Point3D, val vector: Vector3): BaseEntity(){
    companion object {
        // 把被LongPress的正交空间中的点映射到三维空间的一条直线：直线的近端映射到
        // 投影矩阵中定义的视椎体的近平面，直线的远端映射到视椎体的远平面。
        fun convertNormalized2DPointToRay(
            invertedViewProjectionMat: Mat4,
            normalizedX: Float,
            normalizedY: Float
        ): Ray {
            // We'll convert these normalized device coordinates into world-space
            // coordinates. We'll pick a point on the near and far planes, and draw a
            // line between them. To do this transform, we need to first multiply by
            // the inverse matrix, and then we need to undo the perspective divide.
            val nearPointNdc = Vector4(normalizedX, normalizedY, -1f, 1f)
            val farPointNdc = Vector4(normalizedX, normalizedY, 1f, 1f)

            val nearPointWorld = (invertedViewProjectionMat * nearPointNdc).toPoint3D()!!
            val farPointWorld = (invertedViewProjectionMat * farPointNdc).toPoint3D()!!
            LOG.info("convertNormalized2DPointToRay() CoordsInWorld: near=$nearPointWorld, far=$farPointWorld")

            return Ray(
                nearPointWorld,
                Vector3.vectorBetween(nearPointWorld, farPointWorld)
            )
        }
    }
}

// 顶点顺序按逆时针
class Triangle3D(val point0: Point3D, val point1: Point3D, val point2: Point3D) {
    // 三角形构成的平面的法线
    fun normal() =
        Vector3(point0, point1)
            .crossProduct(Vector3(point0, point2)).normalize()

}

class Cylinder(val center: Point3D, val radius: Float, val height: Float)

class Sphere(var center: Point3D, var radius: Float)

class Plane(val point: Point3D, val normal: Vector3)



