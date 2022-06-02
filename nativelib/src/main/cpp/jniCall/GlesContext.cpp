
#include <jni.h>
#include "../nativeCode/renderer/GLESNative.h"

/* [Function definition] */
#ifdef __cplusplus
extern "C" {
#endif

// global pointer is used in JNI calls to reference to same object of type Cube
GLESNative *gGlesObject = nullptr;

JNIEXPORT jboolean JNICALL
Java_com_focus617_nativelib_NativeLib_00024Companion_isInitDoneNative(
        JNIEnv *env, jobject thiz) {

    if (gGlesObject == nullptr || !gGlesObject->IsInitsDone()) {
        return JNI_FALSE;
    } else {
        return JNI_TRUE;
    }
}

JNIEXPORT jstring JNICALL
Java_com_focus617_nativelib_NativeLib_00024Companion_getGLESVersionNative(
        JNIEnv *env, jobject thiz) {

    std::string retValue = "";
    if (gGlesObject == nullptr) {
        return env->NewStringUTF(retValue.c_str());
    }
    retValue = gGlesObject->GetGLESVersionInfo();
    return env->NewStringUTF(retValue.c_str());
}

JNIEXPORT void JNICALL
Java_com_focus617_nativelib_NativeLib_00024Companion_createObjectNative(
        JNIEnv *env, jobject thiz,
        jobject asset_manager,
        jstring path_to_internal_dir) {

    gGlesObject = new GLESNative();
}

JNIEXPORT void JNICALL
Java_com_focus617_nativelib_NativeLib_00024Companion_deleteObjectNative(
        JNIEnv *env, jobject thiz) {

    if (gGlesObject != nullptr) {
        delete gGlesObject;
    }
    gGlesObject = nullptr;
}

#ifdef __cplusplus
}
#endif
/* [Function definition] */


