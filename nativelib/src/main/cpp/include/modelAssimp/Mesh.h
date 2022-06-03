//
// Created by Xu Zhiyong on 2022/6/1.
//

#ifndef XAPP_MESH_H
#define XAPP_MESH_H

#include "../../../../../../../../../Tools/Android/Sdk/ndk/24.0.8215888/toolchains/llvm/prebuilt/windows-x86_64/sysroot/usr/include/c++/v1/string"
#include "../../../../../../../../../Tools/Android/Sdk/ndk/24.0.8215888/toolchains/llvm/prebuilt/windows-x86_64/sysroot/usr/include/c++/v1/map"
#include "../../../../../../../../../Tools/Android/Sdk/ndk/24.0.8215888/toolchains/llvm/prebuilt/windows-x86_64/sysroot/usr/include/c++/v1/vector"

#include "../../../vendors/assimp-5.2.4/include/assimp/Importer.hpp"  // C++ importer interface
#include "../../../vendors/assimp-5.2.4/include/assimp/scene.h"       // Output data structure
#include "../../../vendors/assimp-5.2.4/include/assimp/postprocess.h" // Post processing flags

#include "../common/MeshCommon.h"

#include "../Core.h"
#include "Material.h"

class Mesh : public MeshCommon {
public:
    Mesh() {};

    ~Mesh();

    bool LoadMesh(const std::string &Filename);

    void Render(IRenderCallbacks *pRenderCallbacks = NULL);

    void Render(uint DrawIndex, uint PrimID);

    void Render(uint NumInstances, const Matrix4f *WVPMats, const Matrix4f *WorldMats);

    const Material &GetMaterial();

    void GetLeadingVertex(uint DrawIndex, uint PrimID, Vector3f &Vertex);

protected:

    void Clear();

    virtual void ReserveSpace(uint NumVertices, uint NumIndices);

    virtual void InitSingleMesh(uint MeshIndex, const aiMesh *paiMesh);

    virtual void PopulateBuffers();

    struct BasicMeshEntry {
        BasicMeshEntry() {
            NumIndices = 0;
            BaseVertex = 0;
            BaseIndex = 0;
            MaterialIndex = INVALID_MATERIAL;
        }

        uint NumIndices;
        uint BaseVertex;
        uint BaseIndex;
        uint MaterialIndex;
    };

    std::vector<BasicMeshEntry> m_Meshes;

    const aiScene *m_pScene;

    Matrix4f m_GlobalInverseTransform;

private:

    bool InitFromScene(const aiScene *pScene, const std::string &Filename);

    void CountVerticesAndIndices(const aiScene *pScene, uint &NumVertices, uint &NumIndices);

    void InitAllMeshes(const aiScene *pScene);

    bool InitMaterials(const aiScene *pScene, const std::string &Filename);

    void LoadTextures(const string &Dir, const aiMaterial *pMaterial, int index);

    void LoadDiffuseTexture(const string &Dir, const aiMaterial *pMaterial, int index);

    void LoadSpecularTexture(const string &Dir, const aiMaterial *pMaterial, int index);

    void LoadColors(const aiMaterial *pMaterial, int index);

    enum BUFFER_TYPE {
        INDEX_BUFFER = 0,
        POS_VB = 1,
        TEXCOORD_VB = 2,
        NORMAL_VB = 3,
        WVP_MAT_VB = 4,  // required only for instancing
        WORLD_MAT_VB = 5,  // required only for instancing
        NUM_BUFFERS = 6
    };

    GLuint m_VAO = 0;
    GLuint m_Buffers[NUM_BUFFERS] = {0};

    std::vector<Material> m_Materials;

    // Temporary space for vertex stuff before we load them into the GPU
    vector<Vector3f> m_Positions;
    vector<Vector3f> m_Normals;
    vector<Vector2f> m_TexCoords;
    vector<uint> m_Indices;

    Assimp::Importer m_Importer;
};


#endif //XAPP_MESH_H
