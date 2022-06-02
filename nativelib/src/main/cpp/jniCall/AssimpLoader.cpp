//
// Created by Xu Zhiyong on 2022/6/1.
//

#include <jni.h>

#include "AssimpLoader.h"
#include <jni.h>
#include <jni.h>




/* [Function definition] */
#ifdef __cplusplus
extern "C" {
#endif

// global pointer is used in JNI calls to reference to same object of type Cube
AssimpLoader *gAssimpObject = NULL;

JNIEXPORT void JNICALL
Java_com_focus617_nativelib_NativeLib_00024Companion_createObjectNative(
        JNIEnv *env, jobject thiz,
        jobject asset_manager,
        jstring path_to_internal_dir) {
    // TODO: implement createObjectNative()
}

JNIEXPORT void JNICALL
Java_com_focus617_nativelib_NativeLib_00024Companion_deleteObjectNative(
        JNIEnv *env, jobject thiz) {
    // TODO: implement DeleteObjectNative()
}

#ifdef __cplusplus
}
#endif
/* [Function definition] */

