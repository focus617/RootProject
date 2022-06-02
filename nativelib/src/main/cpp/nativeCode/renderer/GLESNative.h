/**
 * OpenGL ES Native renderer
 */

#ifndef XAPP_GLESNATIVE_H
#define XAPP_GLESNATIVE_H

#include <string>

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


#endif //XAPP_GLESNATIVE_H
