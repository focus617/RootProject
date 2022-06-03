//
// Created by Xu Zhiyong on 2022/6/2.
//

#ifndef XAPP_MODELASSIMP_H
#define XAPP_MODELASSIMP_H

#include "../../../../../../../../../Tools/Android/Sdk/ndk/24.0.8215888/toolchains/llvm/prebuilt/windows-x86_64/sysroot/usr/include/c++/v1/sstream"
#include "../../../../../../../../../Tools/Android/Sdk/ndk/24.0.8215888/toolchains/llvm/prebuilt/windows-x86_64/sysroot/usr/include/c++/v1/iostream"
#include "../../../../../../../../../Tools/Android/Sdk/ndk/24.0.8215888/toolchains/llvm/prebuilt/windows-x86_64/sysroot/usr/include/c++/v1/stdio.h"
#include "../../../../../../../../../Tools/Android/Sdk/ndk/24.0.8215888/toolchains/llvm/prebuilt/windows-x86_64/sysroot/usr/include/c++/v1/string"

#include "../Core.h"
#include "AssimpLoader.h"

class ModelAssimp {
public:
    ModelAssimp();
    ~ModelAssimp();
    void    PerformGLInits();
    void    Render();
    void    SetViewport(int width, int height);
    void    DoubleTapAction();
    void    ScrollAction(float distanceX, float distanceY, float positionX, float positionY);
    void    ScaleAction(float scaleFactor);
    void    MoveAction(float distanceX, float distanceY);
    int     GetScreenWidth() const { return screenWidth; }
    int     GetScreenHeight() const { return screenHeight; }

private:
    bool    initsDone;
    int     screenWidth, screenHeight;

//    std::vector<float> modelDefaultPosition;
//    MyGLCamera * myGLCamera;
    AssimpLoader * modelObject;
};

#endif //XAPP_MODELASSIMP_H
