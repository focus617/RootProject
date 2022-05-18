package com.focus617.core.engine.scene_graph

import com.focus617.core.engine.math.Mat4
import com.focus617.core.engine.math.Vector3

/**
 * SPACE INFORMATION
 */
class Transform {
    //Local space information
    private var mPos: Vector3 = Vector3(0.0f, 0.0f, 0.0f)
    private var mEulerRot: Vector3 = Vector3(0.0f, 0.0f, 0.0f)
    private var mScale: Vector3 = Vector3(1.0f, 1.0f, 1.0f)

    //Dirty flag
    private var mIsDirty = true

    //Local space information concatenate in matrix
    private val mModelMatrix: Mat4 = Mat4()

    private fun computeLocalModelMatrix() {
        mModelMatrix.transform3D(mPos, mScale, mEulerRot)
        mIsDirty = false
    }

    fun getLocalModelMatrix(): Mat4 {
        if (mIsDirty) computeLocalModelMatrix()
        return mModelMatrix
    }

    fun getLocalPosition() = mPos
    fun getLocalRotation() = mEulerRot
    fun getLocalScale() = mScale

    fun isDirty() = mIsDirty

    fun setLocalPosition(newPosition: Vector3) {
        mPos = newPosition
        mIsDirty = true
    }

    fun setLocalRotation(newEulerRot: Vector3) {
        mEulerRot = newEulerRot
        mIsDirty = true
    }

    fun setLocalScale(newScale: Vector3) {
        mScale = newScale
        mIsDirty = true
    }

    fun reset() {
        mPos = Vector3(0.0f, 0.0f, 0.0f)
        mEulerRot = Vector3(0.0f, 0.0f, 0.0f)
        mScale = Vector3(1.0f, 1.0f, 1.0f)
        computeLocalModelMatrix()
    }
}