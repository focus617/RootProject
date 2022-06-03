//
// Created by Xu Zhiyong on 2022/6/1.
//

#ifndef XAPP_MYJNIHELPER_H
#define XAPP_MYJNIHELPER_H

#include "../../../../../../../../../Tools/Android/Sdk/ndk/24.0.8215888/toolchains/llvm/prebuilt/windows-x86_64/sysroot/usr/include/pthread.h"
#include "../../../../../../../../../Tools/Android/Sdk/ndk/24.0.8215888/toolchains/llvm/prebuilt/windows-x86_64/sysroot/usr/include/c++/v1/stdio.h"
#include "../../../../../../../../../Tools/Android/Sdk/ndk/24.0.8215888/toolchains/llvm/prebuilt/windows-x86_64/sysroot/usr/include/c++/v1/string"
#include "../../../../../../../../../Tools/Android/Sdk/ndk/24.0.8215888/toolchains/llvm/prebuilt/windows-x86_64/sysroot/usr/include/jni.h"
#include "../../../../../../../../../Tools/Android/Sdk/ndk/24.0.8215888/toolchains/llvm/prebuilt/windows-x86_64/sysroot/usr/include/c++/v1/vector"
#include "../../../../../../../../../Tools/Android/Sdk/ndk/24.0.8215888/toolchains/llvm/prebuilt/windows-x86_64/sysroot/usr/include/android/asset_manager_jni.h"

#ifdef __cplusplus
extern "C" {
#endif


/**
* Class MyJNIHelper helps to extract files from the app’s assets.
*/
class MyJNIHelper {

private:
    mutable pthread_mutex_t threadMutex{};
    std::string apkInternalPath;
    AAssetManager *apkAssetManager;

public:
    MyJNIHelper(JNIEnv *env, jobject obj, jobject assetManager, jstring pathToInternalDir);

    ~MyJNIHelper();

    void Init(JNIEnv *env, jobject obj, jobject assetManager, jstring pathToInternalDir);

    bool ExtractAssetReturnFilename(const std::string &assetName, std::string &filename,
                                    bool checkIfFileIsAvailable = false);

    /** Strip out the path and return just the filename */
    static std::string GetFileName(const std::string &fileName);

    /**  Read the shader code from assets */
    static bool ReadShaderCode(std::string &shaderCode, std::string &shaderFileName);

    /** Extract only the directory part from the file name */
    static std::string GetDirectoryName(const std::string &fullFileName);
};

/**
 * For the sake of convenience, we define a global pointer in jniCall/GlesContext.cpp to an
 * object of type MyJNIHelper and instantiate it in jniCall api CreateObjectNative().
 * So that shader/texture helper can use this [gHelperObject] to read resource from assets.
 */
extern MyJNIHelper *gHelperObject;

#ifdef __cplusplus
}
#endif

#endif //XAPP_MYJNIHELPER_H
