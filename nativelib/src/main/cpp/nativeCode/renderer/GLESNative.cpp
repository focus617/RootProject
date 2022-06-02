//
// Created by Xu Zhiyong on 2022/6/2.
//
#include <sstream>
#include <iostream>
#include <stdio.h>
#include <string>

#include <GLES3/gl3.h>

#include "Core.h"
#include "GLESNative.h"


/**
 * Checks for OpenGL errors.Very useful while debugging. Call it as often as required
 */
void CheckGLError(std::string funcName){

    GLenum err = glGetError();
    if (err == GL_NO_ERROR) {
        return;
    } else {
        LOGF("[FAIL GL] %s", funcName.c_str());
    }

    switch(err) {

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
            LOGE( "GL_OUT_OF_MEMORY: not enough memory left to execute command");
            break;

        default:
            LOGE("unlisted error");
            break;
    }
}
