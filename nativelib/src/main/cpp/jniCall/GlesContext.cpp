
#include <jni.h>
#include "nativeCode/renderer/GLESNative.h"
#include "nativeCode/common/MyJNIHelper.h"

/* [Function definition] */
#ifdef __cplusplus
extern "C" {
#endif

// global pointer to instance of MyJNIHelper that is used to read from assets
MyJNIHelper *gHelperObject = nullptr;

// global pointer is used in JNI calls to reference to object in charge of render
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
        JNIEnv *env, jobject instance,
        jobject assetManager,
        jstring pathToInternalDir) {

    gHelperObject = new MyJNIHelper(env, instance, assetManager, pathToInternalDir);

    gGlesObject = new GLESNative();
}

JNIEXPORT void JNICALL
Java_com_focus617_nativelib_NativeLib_00024Companion_deleteObjectNative(
        JNIEnv *env, jobject thiz) {

    if (gHelperObject != nullptr) {
        delete gHelperObject;
    }
    gHelperObject = nullptr;

    if (gGlesObject != nullptr) {
        delete gGlesObject;
    }
    gGlesObject = nullptr;
}

#ifdef __cplusplus
}
#endif
/* [Function definition] */


