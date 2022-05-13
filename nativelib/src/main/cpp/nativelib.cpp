#include <jni.h>
#include <string>
#include <android/log.h>

#define LOG_TAG "libNative"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

extern "C"
{

JNIEXPORT jstring JNICALL
Java_com_focus617_nativelib_NativeLib_stringFromJNI(JNIEnv *env, jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT void JNICALL
Java_com_focus617_nativelib_NativeLib_00024Companion_openGlEsSdkNativeLibraryInit(
        JNIEnv *env, jobject thiz, jint width, jint height
) {
    LOGI("Hello From the Native Side!!");
}

}
