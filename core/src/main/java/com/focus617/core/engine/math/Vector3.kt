package com.focus617.core.engine.math

import java.lang.Math.PI


/**
 * Vector math utilities.
 *
 * @param x x coordinate of a vector
 * @param y y coordinate of a vector
 * @param z z coordinate of a vector
 */
class Vector3(var x: Float, var y: Float, var z: Float) {

    constructor(from: Point3D, to: Point3D) :
            this((to.x - from.x), (to.y - from.y), (to.z - from.z))

    constructor(from: Vector3, to: Vector3) :
            this((to.x - from.x), (to.y - from.y), (to.z - from.z))

    constructor(): this(0f, 0f, 0f)

    @JvmOverloads
    constructor(x: Number, y: Number = x, z: Number = x) : this(
        x.toFloat(), y.toFloat(), z.toFloat()
    )


    override fun toString(): String = "Vector($x, $y, $z)"

    override fun equals(other: Any?): Boolean =
        if(other !is Vector3) false
        else ((x==other.x)&&(y==other.y)&&(z==other.z))

    fun homogeneouCoord(): Vector4 {
        return Vector4(x, y, z, 0F)
    }

    fun toFloatArray() = floatArrayOf(x, y, z)

    //求向量的模的方法
    fun length(): Float = kotlin.math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()

    /** Returns the squared length of the quaternion. */
    fun length2() : Float = x * x + y * y + z * z

    //向量规格化的方法
    fun normalize(): Vector3 = scale(1f / length())

    operator fun plus(other: Vector3) = Vector3(
        x + other.x,
        y + other.y,
        z + other.z
    )

    operator fun minus(other: Vector3) = Vector3(
        x - other.x,
        y - other.y,
        z - other.z
    )

    operator fun times(other: Float) = Vector3(
        (x*other).toFloat(),
        (y*other).toFloat(),
        (z*other).toFloat(),
    )

    // -- Unary arithmetic operators --
    operator fun unaryPlus() = this

    operator fun unaryMinus() = Vector3(-x, -y, -z)

    //求向量叉积的方法
    // http://en.wikipedia.org/wiki/Cross_product
    infix fun crossProduct(other: Vector3) = Vector3(
        y * other.z - z * other.y,
        z * other.x - x * other.z,
        x * other.y - y * other.x
    )

    //求向量点积的方法
    // http://en.wikipedia.org/wiki/Dot_product
    infix fun dotProduct(other: Vector3): Float =
        x * other.x + y * other.y + z * other.z

    fun scale(f: Float) = Vector3(
        x * f,
        y * f,
        z * f
    )

    fun setValue(valueX: Float, valueY: Float, valueZ: Float) {
        x = valueX
        y = valueY
        z = valueZ
    }

    companion object {

        //计算当前向量v1与参考向量v2的夹角
        fun angleBetween(vector: Vector3, ref: Vector3): Float
        {
            // 判断异常
            if((vector.length()==0F)||(ref.length()==0F))  throw Exception()

            val cosine: Float = (vector.dotProduct(ref))/(vector.length()*ref.length())
            val angle: Double =(kotlin.math.acos(cosine)*180)/PI
            return angle.toFloat()
        }

        /** http://mathworld.wolfram.com/Point-LineDistance3-Dimensional.html
         *  Note that this formula treats Ray as if it extended infinitely past
         *  either point.
         *  首先定义射线上的两个点：起始点和结束点，结束点是由起始点与射线的向拯相加而得。
         *  接下来，在这两个点与球体的中心点之间创建一个虚拟三角形，
         *  最后， 通过计算三角形的高就得到了这个距离。
        */
        fun distanceBetween(point: Point3D, ray: Ray): Float {
            val p1ToPoint: Vector3 = vectorBetween(ray.point, point)
            val p2ToPoint: Vector3 =
                vectorBetween(ray.point.translate(ray.vector), point)

            // The length of the cross product gives the area of an imaginary
            // parallelogram having the two vectors as sides. A parallelogram can be
            // thought of as consisting of two triangles, so this is the same as
            // twice the area of the triangle defined by the two vectors.
            // http://en.wikipedia.org/wiki/Cross_product#Geometric_meaning
            val areaOfTriangleTimesTwo = p1ToPoint.crossProduct(p2ToPoint).length()
            val lengthOfBase = ray.vector.length()

            // The area of a triangle is also equal to (base * height) / 2. In
            // other words, the height is equal to (area * 2) / base. The height
            // of this triangle is the distance from the point to the ray.
            return areaOfTriangleTimesTwo / lengthOfBase
        }

        fun vectorBetween(from: Point3D, to: Point3D): Vector3 {
            return Vector3(
                to.x - from.x,
                to.y - from.y,
                to.z - from.z
            )
        }

        // 相交测试
        fun intersects(sphere: Sphere, ray: Ray): Boolean {
            return distanceBetween(sphere.center, ray) < sphere.radius
        }

        // http://en.wikipedia.org/wiki/Line-plane_intersection
        // This also treats rays as if they were infinite. It will return a
        // point full of NaNs if there is no intersection point.
        fun intersectionPoint(ray: Ray, plane: Plane): Point3D {
            val rayToPlaneVector: Vector3 = vectorBetween(ray.point, plane.point)

            val scaleFactor = (rayToPlaneVector.dotProduct(plane.normal)
                    / ray.vector.dotProduct(plane.normal))

            return ray.point.translate(ray.vector.scale(scaleFactor))
        }

        /**
         * 计算圆锥面指定棱顶点法向量的方法
         */
        fun calConeNormal(
            x0: Float, y0: Float, z0: Float,  //A，中心点(底面圆的圆心)
            x1: Float, y1: Float, z1: Float,  //B，底面圆上的某一点
            x2: Float, y2: Float, z2: Float   //C，圆锥中心最高点
        ): Vector3 {
            val a = Vector3(x1 - x0, y1 - y0, z1 - z0) //向量AB
            val b = Vector3(x2 - x0, y2 - y0, z2 - z0) //向量AC
            val c = Vector3(x2 - x1, y2 - y1, z2 - z1) //向量BC
            //先求平面ABC的法向量k
            val k = a.crossProduct(b)
            //将c和k做叉乘，得出所求向量d
            val d = c.crossProduct(k)
            //返回规格化后的法向量
            return d.normalize()
        }

        /**
         * 计算三角形的法向量
         * 通过三角形面两个边向量 0-1，0-2, 求叉积得到此面的法向
         * */
        fun calTriangleNormal(
            x0: Float, y0: Float, z0: Float,  //A
            x1: Float, y1: Float, z1: Float,  //B
            x2: Float, y2: Float, z2: Float   //C
        ): Vector3 {
            val a = Vector3(x1 - x0, y1 - y0, z1 - z0) //向量AB
            val b = Vector3(x2 - x0, y2 - y0, z2 - z0) //向量AC

            //通过求两个向量的叉积计算出此三角形面的法向量
            val vNormal = a.crossProduct(b).normalize()
            return vNormal.normalize()
        }
    }


}
