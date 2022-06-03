
#include <android/asset_manager_jni.h>
#include <iostream>
#include <fstream>

#include "Core.h"
#include "common/MyJNIHelper.h"

MyJNIHelper::MyJNIHelper(
        JNIEnv *env, jobject obj, jobject assetManager, jstring pathToInternalDir
) {
    // get a native instance of the asset manager
    // assetManager is passed from Java and should not be garbage collected!
    apkAssetManager = AAssetManager_fromJava(env, assetManager);

    //Save path to app internal data storage as a std::string in apkInternalPath
    // -- we will extract assets and save here
    const char *cPathToInternalDir;
    cPathToInternalDir = env->GetStringUTFChars(pathToInternalDir, nullptr);
    apkInternalPath = std::string(cPathToInternalDir);
    env->ReleaseStringUTFChars(pathToInternalDir, cPathToInternalDir);

    //mutex for thread safety since the AssetManager calls are not thread safe
    pthread_mutex_init(&threadMutex, nullptr);
}

MyJNIHelper::~MyJNIHelper() {
    pthread_mutex_destroy(&threadMutex);
}

void MyJNIHelper::Init(
        JNIEnv *env, jobject obj, jobject assetManager, jstring pathToInternalDir) {

}

/**
 * Search for a file in assets, extract it, save it in internal storage area,
 * and return the new path.
 *
 * The 3rd parameter checkIfFileIsAvailable is a flag to choose whether to check for older versions
 * of the file in storage. By default it is set to false in the function declaration.
 */
bool MyJNIHelper::ExtractAssetReturnFilename(
        const std::string& assetName, std::string &filename, bool checkIfFileIsAvailable) {

    // construct the filename in internal storage by concatenating with path to internal storage
    filename = apkInternalPath + "/" + GetFileName(assetName);

    // check if the file was previously extracted and is available in app's internal dir
    FILE *file = fopen(filename.c_str(), "rb");
    if (file && checkIfFileIsAvailable) {

        LOGI("Found extracted file in assets: %s", filename.c_str());
        fclose(file);
        pthread_mutex_unlock(&threadMutex);
        return true;

    }

    // let us look for the file in assets directory
    bool result = false;

    // AAsset objects are not thread safe and need to be protected with mutex
    pthread_mutex_lock(&threadMutex);

    // Open file with flag AASSET_MODE_STREAMING to indicate to the AssetManager that we will
    // stream the file. We read the file in chunks of size BUFSIZ bytes and write to an output
    // file in internal storage.
    AAsset *asset =
            AAssetManager_open(apkAssetManager, assetName.c_str(), AASSET_MODE_STREAMING);

    char buf[BUFSIZ];
    int nb_read = 0;
    if (asset != nullptr) {
        FILE *out = fopen(filename.c_str(), "w");
        while ((nb_read = AAsset_read(asset, buf, BUFSIZ)) > 0) {
            fwrite(buf, nb_read, 1, out);
        }
        fclose(out);
        AAsset_close(asset);
        result = true;

        LOGI("Asset extracted: %s", filename.c_str());
    } else {
        LOGE("Asset not found: %s", assetName.c_str());
    }

    pthread_mutex_unlock(&threadMutex);
    return result;

}

/**
 * Strip out the path and return just the filename
 */
std::string MyJNIHelper::GetFileName(const std::string &fileName) {

    // assume filename is of the form "<path>/<name>.<type>"
    std::string::size_type slashIndex = fileName.find_last_of('/');

    std::string onlyName;
    if (slashIndex == std::string::npos) {
        onlyName = fileName.substr(0, std::string::npos);
    } else {
        onlyName = fileName.substr(slashIndex + 1, std::string::npos);
    }

    return onlyName;
}

/**
 * Extract only the directory part from the file name
 */
std::string MyJNIHelper::GetDirectoryName(const std::string &fullFileName) {
    std::string::size_type slashIndex = fullFileName.find_last_of('/');
    std::string directoryName;
    if (slashIndex == std::string::npos) {
        directoryName = ".";
    } else if (slashIndex == 0) {
        directoryName = "/";
    } else {
        directoryName = fullFileName.substr(0, slashIndex);
    }
    return directoryName;
}

/**
 * Read the shader code from assets
 */
bool MyJNIHelper::ReadShaderCode(
        std::string &shaderCode, std::string &shaderFileName) {

    LOGI("Reading shader: %s", shaderFileName.c_str());

    // android shaders are stored in assets
    // read them using MyJNIHelper
    bool isFilePresent =
            gHelperObject->ExtractAssetReturnFilename(shaderFileName, shaderFileName);
    if (!isFilePresent) {
        return false;
    }

    std::ifstream shaderStream(shaderFileName.c_str(), std::ios::in);
    if (shaderStream.is_open()) {
        std::string Line = "";
        while (getline(shaderStream, Line)) {
            shaderCode += "\n" + Line;
        }
        shaderStream.close();
    } else {
        LOGF("Cannot open %s", shaderFileName.c_str());
        return false;
    }

    LOGI("Read successfully");
    return true;
}



