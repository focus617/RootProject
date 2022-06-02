#include <jni.h>

/* [Function definition] */
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL
Java_com_focus617_modeldemo_GestureClass_DoubleTapNative(
        JNIEnv *env, jobject thiz) {
    // TODO: implement DoubleTapNative()
}

JNIEXPORT void JNICALL
Java_com_focus617_modeldemo_GestureClass_ScrollNative(
        JNIEnv *env, jobject thiz,
        jfloat distance_x,
        jfloat distance_y,
        jfloat position_x,
        jfloat position_y) {
    // TODO: implement ScrollNative()
}

JNIEXPORT void JNICALL
Java_com_focus617_modeldemo_GestureClass_ScaleNative(
        JNIEnv *env, jobject thiz,
        jfloat scale_factor) {
    // TODO: implement ScaleNative()
}

JNIEXPORT void JNICALL
Java_com_focus617_modeldemo_GestureClass_MoveNative(
        JNIEnv *env, jobject thiz,
        jfloat distance_x,
        jfloat distance_y) {
    // TODO: implement MoveNative()
}


#ifdef __cplusplus
}
#endif
/* [Function definition] */


