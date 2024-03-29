/**
 * AssimpLoader class contains methods to read a 3D model using Assimp’s C++ API.
 */

#ifndef XAPP_ASSIMPLOADER_H
#define XAPP_ASSIMPLOADER_H

#include "../../../../../../../../../Tools/Android/Sdk/ndk/24.0.8215888/toolchains/llvm/prebuilt/windows-x86_64/sysroot/usr/include/c++/v1/map"
#include "../../../vendors/assimp-5.2.4/include/assimp/Importer.hpp"
#include "../../../vendors/assimp-5.2.4/include/assimp/scene.h"
#include "../../../vendors/assimp-5.2.4/include/assimp/postprocess.h"
#include "../../../../../../../../../Tools/Android/Sdk/ndk/24.0.8215888/toolchains/llvm/prebuilt/windows-x86_64/sysroot/usr/include/GLES3/gl3.h"
#include "../../../../../../../../../Tools/Android/Sdk/ndk/24.0.8215888/toolchains/llvm/prebuilt/windows-x86_64/sysroot/usr/include/c++/v1/vector"

// info used to render a mesh
struct MeshInfo {
    GLuint  textureIndex;
    int     numberOfFaces;
    GLuint  faceBuffer;
    GLuint  vertexBuffer;
    GLuint  textureCoordBuffer;
};

class AssimpLoader {

public:
    AssimpLoader();
    ~AssimpLoader();

    bool Load3DModel(const std::string& modelFilename);
    void Delete3DModel();

private:
    void GenerateGLBuffers();
    bool LoadTexturesToGL(std::string modelFilename);

    std::vector<struct MeshInfo> modelMeshes;       // contains one struct for every mesh in model
    Assimp::Importer *importerPtr;
    const aiScene* scene;                           // assimp's output data structure
    bool isObjectLoaded;

    std::map<std::string, GLuint> textureNameMap;   // (texture filename, texture name in GL)
};

#endif //XAPP_ASSIMPLOADER_H
