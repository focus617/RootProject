package com.focus617.core.engine.scene_graph.core

import com.focus617.core.engine.math.Mat4
import com.focus617.core.engine.math.Vector3

/**
 * SPACE INFORMATION
 */
class Transform {
    //Local space information
    var mPos: Vector3 = Vector3(0.0f, 0.0f, 0.0f)
        private set
    var mEulerRot: Vector3 = Vector3(0.0f, 0.0f, 0.0f)
        private set
    var mScale: Vector3 = Vector3(1.0f, 1.0f, 1.0f)
        private set

    //Local space information concatenate in matrix
    private val mCachedLocalModelMatrix: Mat4 = Mat4()

    //Global space information concatenate in matrix
    var mWorldModelMatrix: Mat4 = Mat4()

    /** Dirty flag: Determines when various aspects of the node's transform are dirty
     *  and must be recalculated. */
    private var dirtyTransformFlags: Int = LOCAL_TRANSFORM_DIRTY

    private fun isDirty() = (dirtyTransformFlags != 0)

    private fun isLocalDirty() = ((dirtyTransformFlags and LOCAL_TRANSFORM_DIRTY) != 0)

    private fun setLocalDirty() {
        dirtyTransformFlags = dirtyTransformFlags or LOCAL_TRANSFORM_DIRTY
    }

    private fun clearLocalDirty() {
        dirtyTransformFlags = dirtyTransformFlags xor LOCAL_TRANSFORM_DIRTY
    }

    private fun computeLocalModelMatrix() {
        mCachedLocalModelMatrix.transform3D(mPos, mScale, mEulerRot)
        clearLocalDirty()
    }

    fun getLocalModelMatrix(): Mat4 {
        if (isLocalDirty()) computeLocalModelMatrix()
        return mCachedLocalModelMatrix
    }

    fun setLocalPosition(newPosition: Vector3) {
        mPos = newPosition
        setLocalDirty()
    }

    fun setLocalRotation(newEulerRot: Vector3) {
        mEulerRot = newEulerRot
        setLocalDirty()
    }

    fun setLocalScale(newScale: Vector3) {
        mScale = newScale
        setLocalDirty()
    }

    fun reset() {
        mPos = Vector3(0.0f, 0.0f, 0.0f)
        mEulerRot = Vector3(0.0f, 0.0f, 0.0f)
        mScale = Vector3(1.0f, 1.0f, 1.0f)
        computeLocalModelMatrix()
    }

    companion object {
        private const val LOCAL_TRANSFORM_DIRTY = 1
        private const val WORLD_TRANSFORM_DIRTY = 1 shl 1
        private const val WORLD_INVERSE_TRANSFORM_DIRTY = 1 shl 2
        private const val WORLD_POSITION_DIRTY = 1 shl 3
        private const val WORLD_ROTATION_DIRTY = 1 shl 4
        private const val WORLD_SCALE_DIRTY = 1 shl 5

        private const val WORLD_DIRTY_FLAGS = (WORLD_TRANSFORM_DIRTY
                or WORLD_INVERSE_TRANSFORM_DIRTY
                or WORLD_POSITION_DIRTY
                or WORLD_ROTATION_DIRTY
                or WORLD_SCALE_DIRTY)

        private const val LOCAL_DIRTY_FLAGS = LOCAL_TRANSFORM_DIRTY or WORLD_DIRTY_FLAGS
    }
}