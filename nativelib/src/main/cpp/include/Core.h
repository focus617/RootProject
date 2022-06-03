//
// Created by Xu Zhiyong on 2022/5/31.
//

#ifndef XAPP_CORE_H
#define XAPP_CORE_H

#include <cstring>
#include <string>

#include "android/log.h"
#include "GLES3/gl3.h"

using namespace std;

#define LOG_TAG "libNative"
#define LOGV(...)  __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG,__VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGW(...)  __android_log_print(ANDROID_LOG_WARN, LOG_TAG,__VA_ARGS__)
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

#define NOT_IMPLEMENTED \
    printf("Not implemented case in %s:%d\n", __FILE__, __LINE__); \
    exit(0);


#define GLExitIfError                                                          \
{                                                                               \
    GLenum Error = glGetError();                                                \
                                                                                \
    if (Error != GL_NO_ERROR) {                                                 \
        printf("OpenGL error in %s:%d: 0x%x\n", __FILE__, __LINE__, Error);     \
        exit(0);                                                                \
    }                                                                           \
}

#define GLCheckError() (glGetError() == GL_NO_ERROR)

/** Checks for OpenGL errors.Very useful while debugging. Call it as often as required */
void CheckGLError(const std::string& funcName, const std::string& file, int line);

#define CHECK_GL_ERRORS

#ifdef CHECK_GL_ERRORS
#define GCE CheckGLError(__FUNCTION__, __FILE__, __LINE__);
#else
#define GCE
#endif



#define ZERO_MEM(a) memset(a, 0, sizeof(a))
#define ZERO_MEM_VAR(var) memset(&var, 0, sizeof(var))
#define ARRAY_SIZE_IN_ELEMENTS(a) (sizeof(a)/sizeof(a[0]))

#define SAFE_DELETE(p) if (p) { delete p; p = NULL; }

#ifndef MAX
#define MAX(a,b) ((a) > (b) ? (a) : (b))
#endif

#define RANDOM rand

#define INVALID_UNIFORM_LOCATION 0xffffffff
#define INVALID_MATERIAL 0xFFFFFFFF

#define ASSIMP_LOAD_FLAGS (aiProcess_Triangulate | aiProcess_GenSmoothNormals |  aiProcess_JoinIdenticalVertices )

#endif //XAPP_CORE_H
