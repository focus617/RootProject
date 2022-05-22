package com.focus617.opengles.terrain

import android.content.Context
import android.graphics.Color
import com.focus617.core.engine.math.Point3D
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.mesh.GeneratedMeshData
import com.focus617.core.engine.renderer.vertex.BufferElement
import com.focus617.core.engine.renderer.vertex.BufferLayout
import com.focus617.core.engine.renderer.vertex.ShaderDataType
import com.focus617.core.engine.scene_graph.renderer.DynamicCreationMesh
import com.focus617.platform.helper.BitmapHelper
import timber.log.Timber

class HeightmapMesh(
    val context: Context, private val filePath: String
) : DynamicCreationMesh() {

    private var width: Int = 0
    private var height: Int = 0
    private var numElements: Int = 0

    override fun beforeBuild() {
        val bitmap = BitmapHelper.bitmapLoader(context, filePath)
        width = bitmap.width
        height = bitmap.height

        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        bitmap.recycle()

        Timber.i("Terrain Map (width=${width},  height=${height})")
        if (width * height > 65536) {
            throw RuntimeException("Heightmap is too large for the index buffer.")
        }
        numElements = calculateNumElements()
        Timber.i("\tnumElements:${numElements}")

        val verticesLayout = BufferLayout(
            listOf(
                BufferElement("a_Position", ShaderDataType.Float3, true),
                BufferElement("a_Normal", ShaderDataType.Float3, false),
                BufferElement("a_TexCoord", ShaderDataType.Float2, true)
            )
        )

        val vertices: FloatArray = loadBitmapData(pixels, verticesLayout.getStride())
        val indices: ShortArray = createIndexData()

//        Timber.i("vertices(size=${vertices.size})")
//        for (i in 0..32) Timber.i("$i : ${vertices[i]}")
//
//        Timber.i("indices(size=${indices.size})")
//        for (i in 0..11) Timber.i("$i : ${indices[i]}")

        buildData = GeneratedMeshData(width * height, vertices, verticesLayout, indices)
    }


    /**
     * Copy the heightmap data into a vertex buffer object.
     */
    private fun loadBitmapData(pixels: IntArray, stride: Int): FloatArray {

        val heightmapVertices = FloatArray(width * height * stride)

        var offset = 0
        for (row in 0 until height) {
            for (col in 0 until width) {
                // The heightmap will lie flat on the XZ plane and centered
                // around (0, 0), with the bitmap width mapped to X and the
                // bitmap height mapped to Z, and Y representing the height. We
                // assume the heightmap is grayscale, and use the value of the
                // red color to determine the height.
                // 位图的左上角将被映射到(-0.5, -0.5), 右下角会被映射到(0.5, 0.5)
                val point = getPoint(pixels, row, col)

                heightmapVertices[offset++] = point.x
                heightmapVertices[offset++] = point.y
                heightmapVertices[offset++] = point.z

                // Calculate normal
                val top: Point3D = getPoint(pixels, row - 1, col)
                val left: Point3D = getPoint(pixels, row, col - 1)
                val right: Point3D = getPoint(pixels, row, col + 1)
                val bottom: Point3D = getPoint(pixels, row + 1, col)

                val rightToLeft: Vector3 = Vector3.vectorBetween(right, left)
                val topToBottom: Vector3 = Vector3.vectorBetween(top, bottom)
                val normal: Vector3 = rightToLeft.crossProduct(topToBottom).normalize()

                heightmapVertices[offset++] = normal.x
                heightmapVertices[offset++] = normal.y
                heightmapVertices[offset++] = normal.z

                // Texture coordinates
                heightmapVertices[offset++] = point.x * 50f
                heightmapVertices[offset++] = point.z * 50f
            }
        }
        return heightmapVertices
    }

    /**
     * Returns a point at the expected position given by row and col, but if the
     * position is out of bounds, then it clamps the position and uses the
     * clamped position to read the height. For example, calling with row = -1
     * and col = 5 will set the position as if the point really was at -1 and 5,
     * but the height will be set to the heightmap height at (0, 5), since (-1,
     * 5) is out of bounds. This is useful when we're generating normals, and we
     * need to read the heights of neighbouring points.
     */
    private fun getPoint(pixels: IntArray, row: Int, col: Int): Point3D {
        var rowValue = row
        var colValue = col

        val x = colValue.toFloat() / (width - 1).toFloat() - 0.5f
        val z = rowValue.toFloat() / (height - 1).toFloat() - 0.5f

        rowValue = clamp(rowValue, 0, width - 1)
        colValue = clamp(colValue, 0, height - 1)

        //将范围在[0,255]的灰度值映射到高度坐标，范围[0,1]
        val y = Color.red(pixels[rowValue * height + colValue]).toFloat() / 255.toFloat()

        return Point3D(x, y, z)
    }

    private fun clamp(value: Int, min: Int, max: Int): Int {
        return kotlin.math.max(min, kotlin.math.min(max, value))
    }

    private fun calculateNumElements(): Int {
        // There should be 2 triangles for every group of 4 vertices, so a
        // heightmap of, say, 10x10 pixels would have 9x9 groups, with 2
        // triangles per group and 3 vertices per triangle for a total of (9 x 9
        // x 2 x 3) indices.
        return (width - 1) * (height - 1) * 2 * 3
    }

    /**
     * Create an index buffer object for the vertices to wrap them together into
     * triangles, creating indices based on the width and height of the
     * heightmap.
     */
    private fun createIndexData(): ShortArray {

        val indexData = ShortArray(numElements)
        var offset = 0

        for (row in 0 until height - 1) {
            for (col in 0 until width - 1) {
                // Note: The (short) cast will end up under flowing the number
                // into the negative range if it doesn't fit, which gives us the
                // right unsigned number for OpenGL due to two's complement.
                // This will work so long as the heightmap contains 65536 pixels
                // or less.
                val topLeftIndexNum = (row * width + col).toShort()
                val topRightIndexNum = (row * width + col + 1).toShort()
                val bottomLeftIndexNum = ((row + 1) * width + col).toShort()
                val bottomRightIndexNum = ((row + 1) * width + col + 1).toShort()

                // Write out two triangles.
                indexData[offset++] = topLeftIndexNum
                indexData[offset++] = bottomLeftIndexNum
                indexData[offset++] = topRightIndexNum

                indexData[offset++] = topRightIndexNum
                indexData[offset++] = bottomLeftIndexNum
                indexData[offset++] = bottomRightIndexNum
            }
        }
        return indexData
    }
}
