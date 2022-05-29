package com.focus617.core.engine.math.quat

import com.focus617.core.engine.math.Mat4
import com.focus617.core.engine.math.Trigonometric.atan
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.math.Vector4
import com.focus617.core.engine.math.quat.QuaternionTrigonometric.angleAxis
import java.io.PrintStream
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Following https://github.com/kotlin-graphics/glm
 */
class Quat(w: Float, x: Float, y: Float, z: Float) : QuatT<Float>(w, x, y, z) {

    // -- Implicit basic constructors --
    constructor() : this(1f, 0f, 0f, 0f)
    constructor(f: Float) : this(f, f, f, f)
    constructor(q: Quat) : this(q.w, q.x, q.y, q.z)
    constructor(q: QuatT<out Number>) : this(q.w as Float, q.x as Float, q.y as Float, q.z as Float)
    constructor(s: Float, v: Vector3) : this(s, v.x, v.y, v.z)


    constructor(block: (Int) -> Float) : this(block(0), block(1), block(2), block(3))

    constructor(eulerAngle: Vector3) : this() {
        val eX = eulerAngle.x * .5f
        val eY = eulerAngle.y * .5f
        val eZ = eulerAngle.z * .5f
        val cX = cos(eX)
        val cY = cos(eY)
        val cZ = cos(eZ)
        val sX = sin(eX)
        val sY = sin(eY)
        val sZ = sin(eZ)
        w = cX * cY * cZ + sX * sY * sZ
        x = sX * cY * cZ - cX * sY * sZ
        y = cX * sY * cZ + sX * cY * sZ
        z = cX * cY * sZ - sX * sY * cZ
    }

    constructor(Vector4: Vector4) : this(Vector4.w, Vector4.x, Vector4.y, Vector4.z)

    // -- Explicit basic constructors --
    constructor(w: Number, x: Number, y: Number, z: Number) : this(
        w.toFloat(),
        x.toFloat(),
        y.toFloat(),
        z.toFloat()
    )

    // -- Quat func --
    override fun equals(other: Any?) =
        other is Quat && this[0] == other[0] && this[1] == other[1] && this[2] == other[2] && this[3] == other[3]

    override fun hashCode() =
        31 * (31 * (31 * w.hashCode() + x.hashCode()) + y.hashCode()) + z.hashCode()

    // -- Component accesses --
    override operator fun set(index: Int, value: Number) = when (index) {
        0 -> x = value.toFloat()
        1 -> y = value.toFloat()
        2 -> z = value.toFloat()
        3 -> w = value.toFloat()
        else -> throw ArrayIndexOutOfBoundsException()
    }

    fun setValue(w: Float, x: Float, y: Float, z: Float): Quat {
        this.w = w
        this.x = x
        this.y = y
        this.z = z
        return this
    }

    infix fun setValue(quat: Quat) = setValue(quat.w, quat.x, quat.y, quat.z)

    // -- Unary arithmetic operators --
    operator fun unaryPlus() = this

    operator fun unaryMinus() = Quat(-w, -x, -y, -z)

    // -- Specific binary arithmetic operators --
    infix operator fun plus(b: Quat) = plus(Quat(), this, b)
    fun plus(b: Quat, res: Quat) = plus(res, this, b)
    infix operator fun plusAssign(b: Quat) {
        plus(this, this, b)
    }

    infix operator fun minus(b: Quat) = minus(Quat(), this, b)
    fun minus(b: Quat, res: Quat) = minus(res, this, b)
    infix operator fun minusAssign(b: Quat) {
        minus(this, this, b)
    }

    infix operator fun times(b: Quat) = times(Quat(), this, b)
    infix operator fun times(b: Float) = times(Quat(), this, b)
    infix operator fun times(b: Vector3) = times(Vector3(), this, b)
    infix operator fun times(b: Vector4) = times(Quat(), this, b)
    fun times(b: Quat, res: Quat) = times(res, this, b)
    fun times(b: Float, res: Quat) = times(res, this, b)
    fun times(b: Vector3, res: Vector3) = times(res, this, b)
    fun times(b: Vector4, res: Quat) = times(res, this, b)

    infix operator fun timesAssign(b: Quat) {
        times(this, this, b)
    }

    infix operator fun timesAssign(b: Float) {
        times(this, this, b)
    }

    infix operator fun timesAssign(b: Vector3) {
        times(b, this, b)
    }

    infix operator fun timesAssign(b: Vector4) {
        times(this, this, b)
    }


    infix operator fun div(b: Float) = div(Quat(), this, b)
    fun div(b: Float, res: Quat) = div(res, this, b)
    infix operator fun divAssign(b: Float) {
        div(this, this, b)
    }


    @JvmOverloads
    fun print(name: String = "", stream: PrintStream = System.out) = stream.print("$name$this")

    @JvmOverloads
    fun println(name: String = "", stream: PrintStream = System.out) = stream.println("$name$this")

    override fun toString(): String = "($w, {$x, $y, $z})"

    fun fromFloatArray(ptr: FloatArray) = Quat(ptr[0], ptr[1], ptr[2], ptr[3])

    fun toFloatArray(ptr: FloatArray) {
        ptr[0] = w
        ptr[1] = x
        ptr[2] = y
        ptr[3] = z
    }

    infix fun to(res: Mat4) = mat4_cast(this, res)
    fun toMat4() = mat4_cast(this, Mat4())

    // -- Quat func --
    fun length() = length(this)

    @JvmOverloads
    fun normalize(res: Quat = Quat()) = normalize(this, res)

    fun normalizeAssign() = normalize(this, this)

    infix fun dot(b: Quat) = dot(this, b)

    companion object {
        const val epsilonF = Float.MIN_VALUE

        @JvmField
        val PI = kotlin.math.PI

        @JvmField
        val PIf = kotlin.math.PI.toFloat()

        @JvmField
        val length = 4

        @JvmField
        val size = length * Float.SIZE_BYTES

        val identity: Quat
            get() = Quat(1f, 0f, 0f, 0f)

        /** Create an identity quaternion. */
        fun quatIdentity(res: Quat = Quat()) = res.setValue(1f, 0f, 0f, 0f)

        /** Returns the length of the quaternion.   */
        fun length(q: Quat): Float = sqrt(dot(q, q))

        /** Returns the squared length of the quaternion. */
        fun length2(q: Quat) = q dot q

        /** Returns the normalized quaternion.  */
        fun normalize(q: Quat, res: Quat = Quat()): Quat {
            val len = length(q)
            if (len <= 0f)   // Problem
                return res.setValue(1f, 0f, 0f, 0f)
            val oneOverLen = 1f / len
            return res.setValue(
                q.w * oneOverLen, q.x * oneOverLen, q.y * oneOverLen, q.z * oneOverLen
            )
        }

        fun plus(res: Quat, a: Quat, b: Quat): Quat {
            res.w = a.w + b.w
            res.x = a.x + b.x
            res.y = a.y + b.y
            res.z = a.z + b.z
            return res
        }

        fun minus(res: Quat, a: Quat, b: Quat): Quat {
            res.w = a.w - b.w
            res.x = a.x - b.x
            res.y = a.y - b.y
            res.z = a.z - b.z
            return res
        }


        fun times(res: Quat, a: Quat, b: Quat): Quat {
            val resW = a.w * b.w - a.x * b.x - a.y * b.y - a.z * b.z
            val resX = a.w * b.x + a.x * b.w + a.y * b.z - a.z * b.y
            val resY = a.w * b.y + a.y * b.w + a.z * b.x - a.x * b.z
            val resZ = a.w * b.z + a.z * b.w + a.x * b.y - a.y * b.x
            return res.setValue(resW, resX, resY, resZ)
        }

        fun times(res: Quat, a: Quat, b: Float): Quat {
            res.w = a.w * b
            res.x = a.x * b
            res.y = a.y * b
            res.z = a.z * b
            return res
        }

        fun times(res: Vector3, a: Quat, b: Vector3): Vector3 {
            val uvX = a.y * b.z - b.y * a.z
            val uvY = a.z * b.x - b.z * a.x
            val uvZ = a.x * b.y - b.x * a.y
            val uuvX = a.y * uvZ - uvY * a.z
            val uuvY = a.z * uvX - uvZ * a.x
            val uuvZ = a.x * uvY - uvX * a.y
            res.x = b.x + (uvX * a.w + uuvX) * 2f
            res.y = b.y + (uvY * a.w + uuvY) * 2f
            res.z = b.z + (uvZ * a.w + uuvZ) * 2f
            return res
        }

        fun times(res: Vector3, a: Vector3, b: Quat): Vector3 {
            val dot = dot(b, b)
            val iW = b.w / dot
            val iX = -b.x / dot
            val iY = -b.y / dot
            val iZ = -b.z / dot
            val uvX = iY * a.z - a.y * iZ
            val uvY = iZ * a.x - a.z * iX
            val uvZ = iX * a.y - a.x * iY
            val uuvX = iY * uvZ - uvY * iZ
            val uuvY = iZ * uvX - uvZ * iX
            val uuvZ = iX * uvY - uvX * iY
            res.x = a.x + (uvX * iW + uuvX) * 2f
            res.y = a.y + (uvY * iW + uuvY) * 2f
            res.z = a.z + (uvZ * iW + uuvZ) * 2f
            return res
        }

        fun times(res: Quat, a: Quat, b: Vector4): Quat {
            res.w = a.w
            res.x = a.x * b.x
            res.y = a.y * b.y
            res.z = a.z * b.z
            return res
        }

        fun div(res: Quat, a: Quat, b: Float): Quat {
            res.w = a.w / b
            res.x = a.x / b
            res.y = a.y / b
            res.z = a.z / b
            return res
        }

        /** Returns dot product of q1 main.and q2, main.getI.e., q1[0] * q2[0] + q1[1] * q2[1] + ... */
        fun dot(a: Quat, b: Quat): Float = a.x * b.x + a.y * b.y + a.z * b.z + a.w * b.w


        fun cross(q1: Quat, q2: Quat, res: Quat = Quat()): Quat {
            res.w = q1.w * q2.w - q1.x * q2.x - q1.y * q2.y - q1.z * q2.z
            res.x = q1.w * q2.x + q1.x * q2.w + q1.y * q2.z - q1.z * q2.y
            res.y = q1.w * q2.y + q1.y * q2.w + q1.z * q2.x - q1.x * q2.z
            res.z = q1.w * q2.z + q1.z * q2.w + q1.x * q2.y - q1.y * q2.x
            return res
        }

        /**
         * Compute a cross product between a quaternion and a vector.
         */
        fun cross(res: Vector3, q: Quat, v: Vector3): Vector3 {
            // inverse(q)
            val dot = dot(q, q)
            val w = q.w / dot
            val x = -q.x / dot
            val y = -q.y / dot
            val z = -q.z / dot
            // inverse(q) * v
            val uvX = y * v.z - v.y * z
            val uvY = z * v.x - v.z * x
            val uvZ = x * v.y - v.x * y
            val uuvX = y * uvZ - uvY * z
            val uuvY = z * uvX - uvZ * x
            val uuvZ = x * uvY - uvX * y
            res.x = v.x + (uvX * w + uuvX) * 2f
            res.y = v.y + (uvY * w + uuvY) * 2f
            res.z = v.z + (uvZ * w + uuvZ) * 2f
            return res
        }

        /** Compute a cross product between a vector and a quaternion.
         *  @see gtx_quaternion */
        fun cross(res: Vector3, v: Vector3, q: Quat): Vector3 { // TODO add to Vector3
            // q * v
            val uvX = q.y * v.z - v.y * q.z
            val uvY = q.z * v.x - v.z * q.x
            val uvZ = q.x * v.y - v.x * q.y
            val uuvX = q.y * uvZ - uvY * q.z
            val uuvY = q.z * uvX - uvZ * q.x
            val uuvZ = q.x * uvY - uvX * q.y
            res.x = v.x + (uvX * q.w + uuvX) * 2f
            res.y = v.y + (uvY * q.w + uuvY) * 2f
            res.z = v.z + (uvZ * q.w + uuvZ) * 2f
            return res
        }

        /**
         * Rotates a 3 components vector by a quaternion.
         */
        fun rotate(q: Quat, v: Vector3) = q * v

        /**
         * Rotates a 4 components vector by a quaternion.
         */
        fun rotate(q: Quat, v: Vector4) = q * v

        /**
         * Extract the real component of a quaternion.
         */
        fun extractRealComponent(q: Quat): Float {
            val w = 1 - q.x * q.x - q.y * q.y - q.z * q.z
            return if (w < 0) 0f else -sqrt(w)
        }

//        /** Converts a quaternion to a 3 * 3 matrix.
//         *  @see gtx_quaternion */
//        fun toMat3(x: Quat) = x.toMat3()
//
        /**
         * Converts a quaternion to a 4 * 4 matrix.
         */
        fun toMat4(x: Quat) = x.toMat4()

        /** Converts a quaternion to a 4 * 4 matrix.    */
        fun mat4_cast(q: Quat, res: Mat4): Mat4 {

            val qxx = q.x * q.x
            val qyy = q.y * q.y
            val qzz = q.z * q.z
            val qxz = q.x * q.z
            val qxy = q.x * q.y
            val qyz = q.y * q.z
            val qwx = q.w * q.x
            val qwy = q.w * q.y
            val qwz = q.w * q.z

            res.setIdentity()
            res[0, 0] = 1f - 2f * (qyy + qzz)
            res[0, 1] = 2f * (qxy + qwz)
            res[0, 2] = 2f * (qxz - qwy)

            res[1, 0] = 2f * (qxy - qwz)
            res[1, 1] = 1f - 2f * (qxx + qzz)
            res[1, 2] = 2f * (qyz + qwx)

            res[2, 0] = 2f * (qxz + qwy)
            res[2, 1] = 2f * (qyz - qwx)
            res[2, 2] = 1f - 2f * (qxx + qyy)

            return res
        }

        fun mat4_cast(q: Quat) = mat4_cast(q, Mat4())


//        /** Converts a 3 * 3 matrix to a quaternion.
//         *  @see gtx_quaternion */
//        fun toQuat(x: Mat3) = x.toQuat()
//
//        /** Converts a 4 * 4 matrix to a quaternion.
//         *  @see gtx_quaternion */
//        fun toQuat(x: Mat4) = x.toQuat()
//
        /**
         * Quaternion interpolation using the rotation short path.
         */
        fun shortMix(x: Quat, y: Quat, a: Float): Quat {

            if (a <= 0) return x
            if (a >= 1) return y

            var fCos = x dot y
            var y2 = Quat(y) //BUG!!! tquat<T> y2;
            if (fCos < 0) {
                y2 = -y
                fCos = -fCos
            }

            //if(fCos > 1.0f) // problem
            val k0: Float
            val k1: Float
            if (fCos > 1 - epsilonF) {
                k0 = 1 - a
                k1 = 0 + a //BUG!!! 1.0f + a;
            } else {
                val fSin = sqrt(1 - fCos * fCos)
                val fAngle = atan(fSin, fCos)
                val fOneOverSin = 1 / fSin
                k0 = sin((1 - a) * fAngle) * fOneOverSin
                k1 = sin((0 + a) * fAngle) * fOneOverSin
            }
            return Quat(
                k0 * x.w + k1 * y2.w,
                k0 * x.x + k1 * y2.x,
                k0 * x.y + k1 * y2.y,
                k0 * x.z + k1 * y2.z
            )
        }

        /**
         * Quaternion normalized linear interpolation.
         */
        fun fastMix(x: Quat, y: Quat, a: Float) = normalize(x * (1 - a) + y * a)

        /**
         * Compute the rotation between two vectors.
         *  param orig vector, needs to be normalized
         *  param dest vector, needs to be normalized
         */
        fun rotation(orig: Vector3, dest: Vector3): Quat {

            val cosTheta = orig dotProduct dest

            if (cosTheta >= 1 - epsilonF)
            // orig and dest point in the same direction
                return quatIdentity()

            if (cosTheta < -1 + epsilonF) {
                /*  special case when vectors in opposite directions :
                    there is no "ideal" rotation axis
                    So guess one; any will do as long as it's perpendicular to start
                    This implementation favors a rotation around the Up axis (Y), since it's often what you want to do. */
                var rotationAxis = Vector3(0, 0, 1) crossProduct orig
                if (rotationAxis.length2() < epsilonF) // bad luck, they were parallel, try again!
                    rotationAxis = Vector3(1, 0, 0) crossProduct orig

                rotationAxis = rotationAxis.normalize()
                return angleAxis(PIf, rotationAxis)
            }

            // Implementation from Stan Melax's Game Programming Gems 1 article
            val rotationAxis = orig crossProduct dest

            val s = sqrt((1 + cosTheta) * 2)
            val invs = 1 / s

            return Quat(
                s * 0.5f,
                rotationAxis.x * invs,
                rotationAxis.y * invs,
                rotationAxis.z * invs
            )
        }

        /**
         * Rotates a quaternion from a vector of 3 components axis main.and an angle.
         */
        fun rotate(q: Quat, angle: Float, vX: Float, vY: Float, vZ: Float, res: Quat): Quat {

            var tmpX = vX
            var tmpY = vY
            var tmpZ = vZ
            // Axis of rotation must be normalised
            val len = sqrt(vX * vX + vY * vY + vZ * vZ)
            if (abs(len - 1f) > 0.001f) {
                val oneOverLen = 1f / len
                tmpX *= oneOverLen
                tmpY *= oneOverLen
                tmpZ *= oneOverLen
            }
            val sin = sin(angle * 0.5f)

            val pW = cos(angle * 0.5f)
            val pX = tmpX * sin
            val pY = tmpY * sin
            val pZ = tmpZ * sin

            val w = q.w * pW - q.x * pX - q.y * pY - q.z * pZ
            val x = q.w * pX + q.x * pW + q.y * pZ - q.z * pY
            val y = q.w * pY + q.y * pW + q.z * pX - q.x * pZ
            val z = q.w * pZ + q.z * pW + q.x * pY - q.y * pX

            res.w = w
            res.x = x
            res.y = y
            res.z = z

            return res
        }

        fun rotate(q: Quat, angle: Float, vX: Float, vY: Float, vZ: Float): Quat =
            rotate(q, angle, vX, vY, vZ, Quat())

        fun rotate(q: Quat, angle: Float, v: Vector3, res: Quat): Quat =
            rotate(q, angle, v.x, v.y, v.z, res)

        fun rotate(q: Quat, angle: Float, v: Vector3): Quat =
            rotate(q, angle, v.x, v.y, v.z, Quat())

    }
}