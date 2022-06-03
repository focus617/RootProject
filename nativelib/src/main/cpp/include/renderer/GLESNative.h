/**
 * OpenGL ES Native renderer
 */

#ifndef XAPP_GLESNATIVE_H
#define XAPP_GLESNATIVE_H

#include "../../../../../../../../../Tools/Android/Sdk/ndk/24.0.8215888/toolchains/llvm/prebuilt/windows-x86_64/sysroot/usr/include/c++/v1/string"
#include "../../../../../../../../../Tools/Android/Sdk/ndk/24.0.8215888/toolchains/llvm/prebuilt/windows-x86_64/sysroot/usr/include/GLES3/gl3.h"

class GLESNative {
public:
    GLESNative();
    void    PerformGLInits();
    void    Render();
    void    SetViewport(int width, int height);
    bool    IsInitsDone(){return initsDone;}
    std::string GetGLESVersionInfo() {return glesVersionInfo;}

private:
    bool        initsDone;
    std::string glesVersionInfo;
};


/** Checks for OpenGL errors.Very useful while debugging. Call it as often as required */
void CheckGLError(const std::string& funcName, const std::string& file, int line);

/* get the attribute location of an input variable in a shader */
GLuint GetAttributeLocation(GLuint programID, std::string variableName);

/* get the location of a uniform variable in a shader */
GLint GetUniformLocation(GLuint programID, std::string uniformName);

#endif //XAPP_GLESNATIVE_H
