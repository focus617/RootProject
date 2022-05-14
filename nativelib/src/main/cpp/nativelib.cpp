#include <jni.h>
#include <android/log.h>

#include <GLES3/gl3.h>

#include <cstdio>
#include <cstdlib>
#include <cmath>
#include <string>

#define LOG_TAG "libNative"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL, LOG_TAG, __VA_ARGS__)

 #define MALLOC_CHECK(ptr_type, ptr, size)                                        \
 {                                                                                \
     ptr = (ptr_type) malloc(size);                                               \
     if (ptr == NULL)                                                             \
     {                                                                            \
         LOGF("Memory allocation error FILE: %s LINE: %i\n", __FILE__, __LINE__); \
         exit(EXIT_FAILURE);                                                      \
     }                                                                            \
 }

 #define REALLOC_CHECK(ptr_type, ptr, size)                                       \
 {                                                                                \
     ptr = (ptr_type) realloc(ptr, size);                                         \
     if (ptr == NULL)                                                             \
     {                                                                            \
         LOGF("Memory allocation error FILE: %s LINE: %i\n", __FILE__, __LINE__); \
         exit(EXIT_FAILURE);                                                      \
     }                                                                            \
 }

 #define FREE_CHECK(ptr) \
 {                       \
     free((void*) ptr);  \
     ptr = NULL;         \
 }

 #define GL_CHECK(x)                                                                         \
 x;                                                                                          \
 {                                                                                           \
     GLenum glError = glGetError();                                                          \
     if(glError != GL_NO_ERROR)                                                              \
     {                                                                                       \
         LOGE("glGetError() = %i (%#.8x) at %s:%i\n", glError, glError, __FILE__, __LINE__); \
         exit(EXIT_FAILURE);                                                                 \
     }                                                                                       \
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


