
#include <sstream>
#include <iostream>
#include <cstdio>
#include <string>

#include "renderer/gl3stub.h"
#include "Core.h"
#include "renderer/GLESNative.h"

/**
 * Checks for OpenGL errors.Very useful while debugging. Call it as often as required
 */
#define EXIT_ON_GL_ERROR
void CheckGLError(const std::string &funcName, const std::string &file, int line) {

    GLenum error = 0;
    bool is_error = false;
    while ((error = glGetError()) != GL_NO_ERROR) {

        LOGF("[FAIL GL] '%s' (%s:%d)\n", funcName.c_str(), file.c_str(), line);

        is_error = true;

        switch (error) {

            case GL_INVALID_ENUM:
                LOGE("GL_INVALID_ENUM: GLenum argument out of range");
                break;

            case GL_INVALID_VALUE:
                LOGE("GL_INVALID_VALUE: numeric argument out of range");
                break;

            case GL_INVALID_OPERATION:
                LOGE("GL_INVALID_OPERATION: operation illegal in current state");
                break;

            case GL_INVALID_FRAMEBUFFER_OPERATION:
                LOGE("GL_INVALID_FRAMEBUFFER_OPERATION: framebuffer object is not complete");
                break;

            case GL_OUT_OF_MEMORY:
                LOGE("GL_OUT_OF_MEMORY: not enough memory left to execute command");
                break;

            default:
                LOGE("unlisted error");
                break;
        }
    }
#ifdef EXIT_ON_GL_ERROR
    if (is_error) {
        exit(1);
    }
#endif
}

/*
 * get the attribute location of an input variable in a shader
 */
GLuint GetAttributeLocation(GLuint programID, std::string variableName) {

    GLint loc = glGetAttribLocation(programID, variableName.c_str());
    if (loc == -1) {
        LOGF("Error in getting attribute: %s", variableName.c_str());
        return (0);
    } else {
        return ((GLuint) loc);
    }
}

/*
 * get the location of a uniform variable in a shader
 */
GLint GetUniformLocation(GLuint programID, std::string uniformName) {

    GLint loc = glGetUniformLocation(programID, uniformName.c_str());
    if (loc == -1) {
        LOGF("error in uniform: %s", uniformName.c_str());
        return loc;
    } else {
        return loc;
    }
}

/**
 * Class constructor
 */
GLESNative::GLESNative() {
    initsDone = false;
    glesVersionInfo = "";
}

/**
 * Perform inits and set various GLES options before rendering image
 */
void GLESNative::PerformGLInits() {

    // Black background
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

    // load the GLES version into a string
    std::ostringstream version_string;
    version_string << glGetString(GL_VERSION) << ", " << glGetString(GL_SHADING_LANGUAGE_VERSION);
    glesVersionInfo = std::string(version_string.str());

    // check if the device supports GLES 3 or GLES 2
    const char *versionStr = (const char *) glGetString(GL_VERSION);
    if (strstr(versionStr, "OpenGL ES 3.") && gl3stubInit()) {
        LOGD("Device supports GLES 3");
    } else {
        LOGD("Device supports GLES 2");
    }

    initsDone = true;

    CheckGLError("GLESNative::PerformGLInits", __FILE__, __LINE__);
}

void GLESNative::SetViewport(int width, int height) {
    // set the viewport
    // function is also called when user changes device orientation
    glViewport(0, 0, width, height);

    CheckGLError("GLESNative::SetViewport", __FILE__, __LINE__);
}

void GLESNative::Render() {

    // black background
    glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

    // clear the screen
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    CheckGLError("GLESNative::Render", __FILE__, __LINE__);
}