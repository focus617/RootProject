//
// Created by Xu Zhiyong on 2022/5/31.
//

#ifndef XAPP_CORE_H
#define XAPP_CORE_H

#include "../../../../../../../../Tools/Android/Sdk/ndk/24.0.8215888/toolchains/llvm/prebuilt/windows-x86_64/sysroot/usr/include/android/log.h"
#include "../../../../../../../../Tools/Android/Sdk/ndk/24.0.8215888/toolchains/llvm/prebuilt/windows-x86_64/sysroot/usr/include/GLES3/gl3.h"

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

#endif //XAPP_CORE_H
