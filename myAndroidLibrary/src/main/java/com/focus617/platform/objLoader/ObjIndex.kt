package com.focus617.platform.objLoader

/**
 * Define the data structure for face index parsing
 */
data class ObjIndex(
    var mVertexIndex: Int,
    var mTexCoordIndex: Int,
    var mNormalIndex: Int
) {
    override fun equals(other: Any?): Boolean {
        return if (other !is ObjIndex) false
        else (mVertexIndex == other.mVertexIndex
                && mTexCoordIndex == other.mTexCoordIndex
                && mNormalIndex == other.mNormalIndex)
    }

    override fun hashCode(): Int {
        val BASE = 17
        val MULTIPLIER = 31
        var result = BASE
        result = MULTIPLIER * result + mVertexIndex
        result = MULTIPLIER * result + mTexCoordIndex
        result = MULTIPLIER * result + mNormalIndex
        return result
    }
}