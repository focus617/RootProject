//
// Created by Xu Zhiyong on 2022/6/2.
//

#ifndef XAPP_GLESNATIVE_H
#define XAPP_GLESNATIVE_H

class GLESNative {
public:
    GLESNative();
    void    PerformGLInits();
    void    Render();
    void    SetViewport(int width, int height);
    bool    IsInitsDone(){return initsDone;}
    std::string GetGLESVersionInfo() {return glesVersionInfo;}

private:
    bool    initsDone;
    std::string glesVersionInfo;
};


#endif //XAPP_GLESNATIVE_H
