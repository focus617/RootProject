#include <jni.h>
#include <android/log.h>

#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>

#include <cstdio>
#include <cstdlib>
#include <cmath>
#include <string>

#define LOG_TAG "libNative"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

void loadFile(const char *filePath, int line);

extern "C"
{

JNIEXPORT jstring JNICALL
Java_com_focus617_nativelib_NativeLib_stringFromJNI(JNIEnv *env, jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT void JNICALL
Java_com_focus617_nativelib_NativeLib_00024Companion_openGlEsSdkNativeLibraryInit(
        JNIEnv *env, jobject thiz, jstring filePath, jint line
) {
    LOGI("Hello From the Native Side!!");

    //convert Java strings to cstrings, and release them when finish process
    const char* filePathC = env->GetStringUTFChars(filePath, nullptr);
    loadFile(filePathC, line);
    env->ReleaseStringUTFChars(filePath, filePathC);
}

}

void loadFile(const char *filePath, int line) {
    LOGI("FilePath=%s(line#%d)", filePath, line);

    FILE * file = fopen(filePath, "r");
    if(file == nullptr)
    {
        LOGE("Failure to loadFile the file");
        return;
    }
    char * fileContent =(char *) malloc(sizeof(char) * 1000);
    fread(fileContent, 1000, 1, file);
    LOGI("%s",fileContent);
    free(fileContent);
    fclose(file);
}

const GLfloat triangleVertices[] = {
        0.0f, 1.0f,
        -1.0f, -1.0f,
        1.0f, -1.0f
};
