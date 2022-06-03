
#include <jni.h>
#include "renderer/GLESNative.h"
#include "renderer/NativeRenderer.h"
#include "common/MyBitmapHelper.h"

/* [Function definition] */
#ifdef __cplusplus
extern "C" {
#endif

extern GLESNative *gGlesObject;

JNIEXPORT void JNICALL
Java_com_focus617_nativelib_NativeLib_00024Companion_surfaceCreatedNative(
        JNIEnv *env, jobject thiz) {

    if (gGlesObject == nullptr) {
        return;
    }
    gGlesObject->PerformGLInits();
}

JNIEXPORT void JNICALL
Java_com_focus617_nativelib_NativeLib_00024Companion_surfaceChangedNative(JNIEnv *env, jobject thiz,
                                                                          jint width, jint height) {
    if (gGlesObject == nullptr) {
        return;
    }
    gGlesObject->SetViewport(width, height);

    setupGraphics(width, height);
}

JNIEXPORT void JNICALL
Java_com_focus617_nativelib_NativeLib_00024Companion_drawFrameNative(
        JNIEnv *env, jobject thiz) {

    if (gGlesObject == nullptr) {
        return;
    }
    gGlesObject->Render();

    renderFrame();
}

JNIEXPORT void JNICALL
Java_com_focus617_nativelib_NativeLib_00024Companion_ndkEmboss(
        JNIEnv *env, jobject thiz, jintArray data, jint width, jint height) {
    Emboss(env, thiz, data, width, height);
}

#ifdef __cplusplus
}
#endif
/* [Function definition] */

