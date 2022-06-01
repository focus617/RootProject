//
// Created by Xu Zhiyong on 2022/6/1.
//

#ifndef XAPP_MYJNIHELPER_H
#define XAPP_MYJNIHELPER_H

#include <pthread.h>
#include <string>
#include <jni.h>
#include <android/asset_manager_jni.h>

#ifdef __cplusplus
extern "C" {
#endif

class MyJNIHelper {

private:
    mutable pthread_mutex_t threadMutex;
    std::string apkInternalPath;
    AAssetManager *apkAssetManager;

public:
    MyJNIHelper(JNIEnv *env, jobject obj, jobject assetManager, jstring pathToInternalDir);

    ~MyJNIHelper();

    void Init(JNIEnv *env, jobject obj, jobject assetManager, jstring pathToInternalDir);

    bool ExtractAssetReturnFilename(std::string assetName, std::string &filename,
                                    bool checkIfFileIsAvailable = false);

    bool ReadFileFromAssetsToBuffer(const char *filename, std::vector<uint8_t> *bufferRef);
};

extern MyJNIHelper *gHelperObject;

#ifdef __cplusplus
}
#endif

#endif //XAPP_MYJNIHELPER_H
