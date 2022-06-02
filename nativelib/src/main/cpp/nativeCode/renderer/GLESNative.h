/**
 * OpenGL ES Native renderer
 */

#ifndef XAPP_GLESNATIVE_H
#define XAPP_GLESNATIVE_H

#include <string>
#include <GLES3/gl3.h>

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
void CheckGLError(const std::string& funcName);

/* get the attribute location of an input variable in a shader */
GLuint GetAttributeLocation(GLuint programID, std::string variableName);

/* get the location of a uniform variable in a shader */
GLint GetUniformLocation(GLuint programID, std::string uniformName);

#endif //XAPP_GLESNATIVE_H
