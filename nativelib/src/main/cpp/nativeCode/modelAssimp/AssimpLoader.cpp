/**
 * AssimpLoader class contains methods to read a 3D model using Assimp’s C++ API.
 */

#include <nativeCode/common/MyJNIHelper.h>
#include "Core.h"
#include "AssimpLoader.h"
#include "nativeCode/renderer/GLESNative.h"

/**
 * Class constructor, loads shaders & gets locations of variables in them
 */
AssimpLoader::AssimpLoader() {
    importerPtr = new Assimp::Importer;
    scene = nullptr;
    isObjectLoaded = false;

    CheckGLError("AssimpLoader::AssimpLoader");
}

/**
 * Class destructor, deletes Assimp importer pointer and removes 3D model from GL
 */
AssimpLoader::~AssimpLoader() {
    Delete3DModel();
    if (importerPtr) {
        delete importerPtr;
        importerPtr = nullptr;
    }
    scene = nullptr; // gets deleted along with importerPtr
}

/**
 * Generate buffers for vertex positions, texture coordinates, faces -- and load data into them
 */
void AssimpLoader::GenerateGLBuffers() {

    struct MeshInfo newMeshInfo{}; // this struct is updated for each mesh in the model
    GLuint buffer;

    // For every mesh -- load face indices, vertex positions, vertex texture coords
    // also copy texture index for mesh into newMeshInfo.textureIndex
    for (unsigned int n = 0; n < scene->mNumMeshes; ++n) {

        const aiMesh *mesh = scene->mMeshes[n]; // read the n-th mesh

        // create array with faces
        // convert from Assimp's format to array for GLES
        auto *faceArray = new unsigned int[mesh->mNumFaces * 3];
        unsigned int faceIndex = 0;
        for (unsigned int t = 0; t < mesh->mNumFaces; ++t) {

            // read a face from assimp's mesh and copy it into faceArray
            const aiFace *face = &mesh->mFaces[t];
            memcpy(&faceArray[faceIndex], face->mIndices, 3 * sizeof(unsigned int));
            faceIndex += 3;

        }
        newMeshInfo.numberOfFaces = scene->mMeshes[n]->mNumFaces;

        // buffer for faces
        if (newMeshInfo.numberOfFaces) {

            glGenBuffers(1, &buffer);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffer);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER,
                         sizeof(unsigned int) * mesh->mNumFaces * 3,
                         faceArray, GL_STATIC_DRAW);
            newMeshInfo.faceBuffer = buffer;
        }
        delete[] faceArray;

        // buffer for vertex positions
        if (mesh->HasPositions()) {

            glGenBuffers(1, &buffer);
            glBindBuffer(GL_ARRAY_BUFFER, buffer);
            glBufferData(GL_ARRAY_BUFFER,
                         sizeof(float) * 3 * mesh->mNumVertices,
                         mesh->mVertices, GL_STATIC_DRAW);
            newMeshInfo.vertexBuffer = buffer;
        }

        // buffer for vertex texture coordinates
        // ***ASSUMPTION*** -- handle only one texture for each mesh
        if (mesh->HasTextureCoords(0)) {

            float *textureCoords = new float[2 * mesh->mNumVertices];
            for (unsigned int k = 0; k < mesh->mNumVertices; ++k) {
                textureCoords[k * 2] = mesh->mTextureCoords[0][k].x;
                textureCoords[k * 2 + 1] = mesh->mTextureCoords[0][k].y;
            }
            glGenBuffers(1, &buffer);
            glBindBuffer(GL_ARRAY_BUFFER, buffer);
            glBufferData(GL_ARRAY_BUFFER,
                         sizeof(float) * 2 * mesh->mNumVertices, textureCoords,
                         GL_STATIC_DRAW);
            newMeshInfo.textureCoordBuffer = buffer;
            delete[] textureCoords;

        }

        // unbind buffers
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        // copy texture index (= texture name in GL) for the mesh from textureNameMap
        aiMaterial *mtl = scene->mMaterials[mesh->mMaterialIndex];
        aiString texturePath;    //contains filename of texture
        if (AI_SUCCESS == mtl->GetTexture(aiTextureType_DIFFUSE, 0, &texturePath)) {
            unsigned int textureId = textureNameMap[texturePath.data];
            newMeshInfo.textureIndex = textureId;
        } else {
            newMeshInfo.textureIndex = 0;
        }

        modelMeshes.push_back(newMeshInfo);
    }
}

/**
 * Read textures associated with all materials and load images to GL
 */
bool AssimpLoader::LoadTexturesToGL(std::string modelFilename) {

    // read names of textures associated with all materials
    textureNameMap.clear();

    for (unsigned int m = 0; m < scene->mNumMaterials; ++m) {

        int textureIndex = 0;
        aiString textureFilename;
        aiReturn isTexturePresent = scene->mMaterials[m]->GetTexture(
                aiTextureType_DIFFUSE, textureIndex, &textureFilename);

        while (isTexturePresent == AI_SUCCESS) {
            //fill map with textures, OpenGL image ids set to 0
            textureNameMap.insert(std::pair<std::string, GLuint>(textureFilename.data, 0));

            // more textures? more than one texture could be associated with a material
            textureIndex++;
            isTexturePresent = scene->mMaterials[m]->GetTexture(
                    aiTextureType_DIFFUSE, textureIndex, &textureFilename);
        }
    }

    int numTextures = (int) textureNameMap.size();
    LOGI("Total number of textures is %d ", numTextures);

    // create and fill array with texture names in GL
    auto *textureGLNames = new GLuint[numTextures];
    glGenTextures(numTextures, textureGLNames);

    // Extract the directory part from the file name
    // will be used to read the texture
    std::string modelDirectoryName = MyJNIHelper::GetDirectoryName(modelFilename);

    // iterate over the textures, read them using OpenCV, load into GL
    std::map<std::string, GLuint>::iterator textureIterator = textureNameMap.begin();
    int i = 0;
    for (; textureIterator != textureNameMap.end(); ++i, ++textureIterator) {

        std::string textureFilename = (*textureIterator).first;  // get filename
        std::string textureFullPath = modelDirectoryName + "/" + textureFilename;
        (*textureIterator).second = textureGLNames[i];      // save texture id for filename in map

        // load the texture image into OpenCV matrix via OpenCV method
        LOGI("Loading texture %s", textureFullPath.c_str());
//        cv::Mat textureImage = cv::imread(textureFullPath);
//        if (!textureImage.empty()) {
//
//            // opencv reads textures in BGR format, change to RGB for GL
//            cv::cvtColor(textureImage, textureImage, CV_BGR2RGB);
//            // opencv reads image from top-left, while GL expects it from bottom-left
//            // vertically flip the image
//            cv::flip(textureImage, textureImage, 0);
//
//            // bind the texture
//            glBindTexture(GL_TEXTURE_2D, textureGLNames[i]);
//            // specify linear filtering
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
//            // load the OpenCV Mat into GLES
//            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, textureImage.cols,
//                         textureImage.rows, 0, GL_RGB, GL_UNSIGNED_BYTE,
//                         textureImage.data);
//            CheckGLError("AssimpLoader::loadGLTexGen");
//
//        } else {

            LOGE("Couldn't load texture %s", textureFilename.c_str());

            //Cleanup and return
            delete[] textureGLNames;
            return false;

//        }
    }

    //Cleanup and return
    delete[] textureGLNames;
    return true;
}


/**
 * Loads a general OBJ with many meshes -- assumes texture is associated with each mesh
 * does not handle material properties (like diffuse, specular, etc.)
 */
bool AssimpLoader::Load3DModel(const std::string &modelFilename) {

    LOGI("Scene will be imported now(model=%s)", modelFilename.c_str());
    scene = importerPtr->ReadFile(modelFilename, aiProcessPreset_TargetRealtime_Quality);

    // Check if import failed
    if (!scene) {
        std::string errorString = importerPtr->GetErrorString();
        LOGE("Scene import failed: %s", errorString.c_str());
        return false;
    }
    LOGI("Imported %s successfully.", modelFilename.c_str());

    if (!LoadTexturesToGL(modelFilename)) {
        LOGE("Unable to load textures");
        return false;
    }
    LOGI("Loaded textures successfully");

    GenerateGLBuffers();
    LOGI("Loaded vertices and texture coords successfully");

    isObjectLoaded = true;
    return true;
}

/**
 * Clears memory associated with the 3D model
 */
void AssimpLoader::Delete3DModel() {
    if (isObjectLoaded) {
        // clear modelMeshes stuff
//        for (unsigned int i = 0; i < modelMeshes.size(); ++i) {
//            glDeleteTextures(1, &(modelMeshes[i].textureIndex));
//        }
        modelMeshes.clear();

        LOGI("Deleted Assimp object");
        isObjectLoaded = false;
    }
}

