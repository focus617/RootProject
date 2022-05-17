package com.focus617.platform.objLoader

import com.focus617.core.engine.math.Vector3

class IndexedModel {
    private var mPositions = ArrayList<Vector3>()
    private var mTexcoords = ArrayList<Vector3>()
    private var mNormals = ArrayList<Vector3>()
    private var mTangents = ArrayList<Vector3>()
    private var mIndices = ArrayList<Int>()

    fun getPositions(): ArrayList<Vector3> = mPositions
    fun getTexCoords(): ArrayList<Vector3> = mTexcoords
    fun getNormals(): ArrayList<Vector3> = mNormals
    fun getTangents(): ArrayList<Vector3> = mTangents
    fun getIndices(): ArrayList<Int> = mIndices

    fun calcNormals() {
        var i = 0
        while (i < mIndices.size) {
            val i0: Int = mIndices[i]
            val i1: Int = mIndices[i + 1]
            val i2: Int = mIndices[i + 2]

            val v1: Vector3 = mPositions[i1] - mPositions[i0]
            val v2: Vector3 = mPositions[i2] - mPositions[i0]
            val normal: Vector3 = v1.crossProduct(v2).normalize()

            mNormals[i0] = mNormals[i0] + normal
            mNormals[i1] = mNormals[i1] + normal
            mNormals[i2] = mNormals[i2] + normal
            i += 3
        }

        for (i in 0 until mNormals.size)
            mNormals[i] = mNormals[i].normalize()
    }
}