package com.focus617.core.engine.math


// 二维
class Triangle2D(val point0: Point2D, val point1: Point2D, val point2: Point2D)

class Circle(val center: Point3D, val radius: Float) {

    fun scale(scale: Float): Circle {
        return Circle(center, radius * scale)
    }
}

// 三维
class Ray(val point: Point3D, val vector: Vector3)

// 顶点顺序按逆时针
class Triangle3D(val point0: Point3D, val point1: Point3D, val point2: Point3D) {
    // 三角形构成的平面的法线
    fun normal() =
        Vector3(point0, point1)
            .crossProduct(Vector3(point0, point2)).normalize()

}

class Cylinder(val center: Point3D, val radius: Float, val height: Float)

class Sphere(val center: Point3D, val radius: Float)

class Plane(val point: Point3D, val normal: Vector3)



