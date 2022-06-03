//
// Created by Xu Zhiyong on 2022/6/2.
//

#ifndef XAPP_MATERIAL_H
#define XAPP_MATERIAL_H

#include "../math/Math3D.h"

class Material {

public:
    Vector3f AmbientColor = Vector3f(0.0f, 0.0f, 0.0f);
    Vector3f DiffuseColor = Vector3f(0.0f, 0.0f, 0.0f);
    Vector3f SpecularColor = Vector3f(0.0f, 0.0f, 0.0f);

    // TODO: need to deallocate these
//    Texture* pDiffuse = NULL; // base color of the material
//    Texture* pSpecularExponent = NULL;
};

#endif //XAPP_MATERIAL_H
