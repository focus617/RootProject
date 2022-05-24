package com.focus617.platform.objLoader

import android.content.Context
import android.text.TextUtils
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.renderer.vertex.BufferElement
import com.focus617.core.engine.renderer.vertex.BufferLayout
import com.focus617.core.engine.renderer.vertex.ShaderDataType
import com.focus617.platform.helper.FileHelper
import timber.log.Timber
import java.util.*

object ObjLoader {
    //模型文件所在目录
    private var mDirectory: String = ""

    // 存放解析出来的 Obj名称
    private var mObjInfoName: String = ""

    // 存放解析出来的 mtl文件名称
    private var mMtlFilePath: String? = null

    private var mPositions = ArrayList<Vector3>()   // 存放解析出来的顶点的坐标
    private var mNormals = ArrayList<Vector3>()     //存放解析出来的法线坐标
    private var mTexCoords = ArrayList<Vector3>()   //存放解析出来的纹理坐标

    private var mHasNormals = false
    private var mHasTexCoords = false
    private var mTextureDimension = 2

    private val mFaceIndices = HashMap<String, ArrayList<ObjIndex>>()   //存放解析出来面的索引
    private var mCurrentIndices: ArrayList<ObjIndex>? = null

    fun clear() {
        mPositions.clear()
        mTexCoords.clear()
        mNormals.clear()
        mFaceIndices.clear()

        mDirectory = ""
        mObjInfoName = ""
        mHasNormals = false
        mHasTexCoords = false
        mTextureDimension = 2
        mMtlFilePath = null
        mCurrentIndices = null
    }

    fun getMtlFilePath() = mMtlFilePath ?: ""

    /**
     * 加载并分析Obj文件，构造 Meshes 和 Materials
     * @param context   Context
     * @param objFilePath assets的obj文件路径
     * @return
     */
    fun loadOBJ(context: Context, objFilePath: String): ObjLoader {
        Timber.i("load model from file: $objFilePath")

        if (objFilePath.isEmpty() or TextUtils.isEmpty(objFilePath)) {
            Timber.e("Obj File doesn't exist")
        }

        if (FileHelper.getFileExt(objFilePath) != "obj") {
            Timber.e("File format not supported for mesh data.")
        }

        mDirectory = FileHelper.getPath(objFilePath)

        parse(context, objFilePath)

        return this
    }


    /**
     * 分析obj文件
     *
     * 解析obj文件时，数据类型由开头的首字母决定
     *   switch(首字母){
     *       case "v":  顶点坐标数据
     *          break;
     *       case "vn": 法向量坐标数据
     *          break;
     *       case "vt": 纹理坐标数据
     *          break;
     *       case "usemtl": 材质
     *          break;
     *       case "f": 面索引
     *          break;
     *   }
     */
    private fun parse(context: Context, objFilePathName: String) {
        clear()

        try {
            val scanner = Scanner(context.assets.open(objFilePathName))
            while (scanner.hasNextLine()) {
                val line = scanner.nextLine().trim()

                when {
                    line.isEmpty() -> continue

                    line.startsWith(ANNOTATION) -> continue  // 注释行

                    line.startsWith(MTLLIB) -> {
                        fillMtlLib(line)
                    }
                    line.startsWith(O) -> {
                        fillObjName(line)
                    }
                    line.startsWith(VN) -> {
                        fillNormals(line)
                    }
                    line.startsWith(VT) -> {
                        fillTexCoords(line)
                    }
                    line.startsWith(V) -> {
                        fillVertexs(line)
                    }
                    line.startsWith(USEMTL) -> {
                        switchUseMtl(line)
                    }
                    line.startsWith(F) -> {
                        fillFaceList(line)
                    }
                    else -> {
                        Timber.i("dropped: $line")
                    }
                }
            }
            scanner.close()
        } catch (ex: Exception) {
            Timber.e("${ex.javaClass} for " + ex.message.toString())
        }
    }

    // 对象名称
    private fun fillObjName(line: String) {
        val tokens = line.split(DELIMITER).toTypedArray()
        if (tokens.size != 2) return
        mObjInfoName = tokens[1]
    }

    // 材质
    private fun fillMtlLib(line: String) {
        val tokens = line.split(DELIMITER).toTypedArray()
        if (tokens.size != 2) return
        if (!TextUtils.isEmpty(tokens[1])) {
            mMtlFilePath = "${mDirectory}/${tokens[1]}"
        }
    }

    private fun fillVertexs(line: String) {
        val tokens = line.split(DELIMITER)
        if (tokens.size != 4) return

        val x = tokens[1].toFloat()
        val y = tokens[2].toFloat()
        val z = tokens[3].toFloat()
        mPositions.add(Vector3(x, y, z))
    }

    /**
     * 分析纹理坐标
     *
     * 这里纹理的Y值，需要(Y = 1-Y0),原因是openGl的纹理坐标系与android的坐标系存在Y值镜像的状态
     */
    private fun fillTexCoords(line: String) {
        val tokens = line.split(DELIMITER)
        when (tokens.size) {
            3 -> mTextureDimension = 2
            4 -> mTextureDimension = 3
            !in 3..4 -> return
        }

        val x = tokens[1].toFloat()
        // 纹理的Y值，需要(Y = 1-Y0)
        val y = 1f - tokens[2].toFloat()
        val z = if (tokens.size == 4) tokens[3].toFloat() else 0f
        mTexCoords.add(Vector3(x, y, z))
    }

    private fun fillNormals(line: String) {
        val tokens = line.split(DELIMITER)
        if (tokens.size != 4) return

        val x = tokens[1].toFloat()
        val y = tokens[2].toFloat()
        val z = tokens[3].toFloat()
        mNormals.add(Vector3(x, y, z))
    }

    private fun switchUseMtl(line: String) {
        val tokens = line.split(DELIMITER)
        if (tokens.size != 2) return

        // Get new material name
        val currentMaterialName = tokens[1]
        Timber.i("switchUseMtl(): $currentMaterialName")

        if (mFaceIndices.containsKey(currentMaterialName)) {
            // switch to old Face Indices based on switched material name
            mCurrentIndices = mFaceIndices[currentMaterialName]!!
            Timber.i("switchUseMtl(): reuse existing face indices for $currentMaterialName")
        } else {
            // create a new Face Indices
            mCurrentIndices = ArrayList<ObjIndex>()
            mFaceIndices[currentMaterialName] = mCurrentIndices!!
            Timber.i("switchUseMtl(): create new face indices for $currentMaterialName")
            Timber.i("switchUseMtl(): now we have total ${mFaceIndices.size} groups.")
        }
    }

    private fun fillFaceList(line: String) {
        val tokens = line.split(DELIMITER)

        if (mCurrentIndices == null) {
            mCurrentIndices = ArrayList<ObjIndex>()
            mFaceIndices["Default"] = mCurrentIndices!!
        }
        //将所有类型，包括多于三角的情形，统一转换为三角Mesh
        for (i in 0 until tokens.size - 3) {
            mCurrentIndices!!.add(parseFaceIndex(tokens[1]))
            mCurrentIndices!!.add(parseFaceIndex(tokens[2 + i]))
            mCurrentIndices!!.add(parseFaceIndex(tokens[3 + i]))
        }
    }

    // format: "vertexIndex[/texCoordIndex/normalIndex] .."
    private fun parseFaceIndex(token: String): ObjIndex {
        val values = token.split("/").toTypedArray()

        val vertexIndex = Integer.valueOf(values[0]) - 1

        var texCoordIndex = 0
        var normalIndex = 0

        if (values.size > 1) {
            if (values[1].isNotEmpty()) {
                mHasTexCoords = true
                texCoordIndex = Integer.parseInt(values[1]) - 1
            }

            if (values.size > 2) {
                mHasNormals = true
                normalIndex = Integer.parseInt(values[2]) - 1
            }
        }
        return ObjIndex(vertexIndex, texCoordIndex, normalIndex)
    }

    fun toVertexArrayModel(): ArrayList<VertexArrayModel> {
        val result = arrayListOf<VertexArrayModel>()

        // Loop for each Group
        for ((key, data) in mFaceIndices) {
            val size = data.size * 9
            val vertices = FloatArray(size)
            val indices = ShortArray(size)
            var verticesPtr = 0

            // Each face in Group
            for (i in 0 until data.size) {
                // store index into indices
                indices[i] = i.toShort()

                // store vertex attributes into vertices
                val currentIndex: ObjIndex = data[i]
                // 1. Vertex
                vertices[verticesPtr++] = mPositions[currentIndex.mVertexIndex].x
                vertices[verticesPtr++] = mPositions[currentIndex.mVertexIndex].y
                vertices[verticesPtr++] = mPositions[currentIndex.mVertexIndex].z
                // 2. Normal
                if (mHasNormals) {
                    vertices[verticesPtr++] = mNormals[currentIndex.mNormalIndex].x
                    vertices[verticesPtr++] = mNormals[currentIndex.mNormalIndex].y
                    vertices[verticesPtr++] = mNormals[currentIndex.mNormalIndex].z
                }
                // 3. Texture
                if (mHasTexCoords) {
                    vertices[verticesPtr++] = mTexCoords[currentIndex.mTexCoordIndex].x
                    vertices[verticesPtr++] = mTexCoords[currentIndex.mTexCoordIndex].y
                    if (mTextureDimension == 3) {
                        vertices[verticesPtr++] = mTexCoords[currentIndex.mTexCoordIndex].z
                    }
                }
            }

            // Build the Buffer Layout(Vertex Attribute List)
            val layout = createBufferLayout()

            // Generate VertexArrayModel
            result.add(VertexArrayModel(key, vertices, layout, indices))
        }
        return result
    }

    // Build the Buffer Layout(Vertex Attribute List)
    private fun createBufferLayout(): BufferLayout {
        val elements = mutableListOf<BufferElement>()
        elements.add(BufferElement("a_Position", ShaderDataType.Float3, true))
        if (mHasNormals) elements.add(
            BufferElement("a_Normal", ShaderDataType.Float3, true)
        )
        if (mHasTexCoords) {
            val type =
                if (mTextureDimension == 2) ShaderDataType.Float2 else ShaderDataType.Float3
            elements.add(BufferElement("a_TexCoord", type, true))
        }
        return BufferLayout(elements.toList())
    }

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

    /**
     * obj需解析字段
     */
    private const val MTLLIB = "mtllib"     // obj对应的材质文件

    private const val ANNOTATION = "#"      // 注释

    private const val G = "g"               // 组名称

    private const val O = "o"               // o 对象名称(Object name)

    private const val V = "v "               // 顶点

    private const val VT = "vt"             // 纹理坐标

    private const val VN = "vn"             // 顶点法线

    private const val USEMTL = "usemtl"     // 使用的材质

    private const val F = "f"   //f v1/vt1/vn1 v2/vt2/vn2 v3/vt3/vn3(索引起始于1)

    private val DELIMITER = Regex("[ ]+")    // 分隔符
}