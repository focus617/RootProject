
#include "Core.h"
#include "modelAssimp/ModelAssimp.h"
#include "renderer/GLESNative.h"
#include "common/MyJNIHelper.h"

/**
 * Class constructor
 */
ModelAssimp::ModelAssimp() {

    LOGD("ModelAssimp::ModelAssimp");
    initsDone = false;

    // create MyGLCamera object and set default position for the object
//    myGLCamera = new MyGLCamera();
    float pos[]={0.,0.,0.,0.2,0.5,0.};
//    std::copy(&pos[0], &pos[5], std::back_inserter(modelDefaultPosition));
//    myGLCamera->SetModelPosition(modelDefaultPosition);

    modelObject = NULL;
}

ModelAssimp::~ModelAssimp() {

    LOGD("ModelAssimp::ModelAssimpssimp");
//    if (myGLCamera) {
//        delete myGLCamera;
//    }
    if (modelObject) {
        delete modelObject;
    }
}

/**
 * Perform inits and load the triangle's vertices/colors to GLES
 */
void ModelAssimp::PerformGLInits() {

    LOGD("ModelAssimp::PerformGLInits");
//
//    MyGLInits();
//
    modelObject = new AssimpLoader();

    // extract the OBJ and companion files from assets
    std::string objFilename, mtlFilename, texFilename;
    bool isFilesPresent  =
            gHelperObject->ExtractAssetReturnFilename("amenemhat/amenemhat.obj", objFilename) &&
            gHelperObject->ExtractAssetReturnFilename("amenemhat/amenemhat.mtl", mtlFilename) &&
            gHelperObject->ExtractAssetReturnFilename("amenemhat/amenemhat.jpg", texFilename);
    if( !isFilesPresent ) {
        LOGE("Model %s does not exist!", objFilename.c_str());
        return;
    }

    modelObject->Load3DModel(objFilename);

    CheckGLError("ModelAssimp::PerformGLInits", __FILE__, __LINE__);
    initsDone = true;
}


/**
 * Render to the display
 */
void ModelAssimp::Render() {

    // clear the screen
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

//    glm::mat4 mvpMat = myGLCamera->GetMVP();
//    modelObject->Render3DModel(&mvpMat);

    CheckGLError("ModelAssimp::Render", __FILE__, __LINE__);

}

/**
 * set the viewport, function is also called when user changes device orientation
 */
void ModelAssimp::SetViewport(int width, int height) {

    screenHeight = height;
    screenWidth = width;
    glViewport(0, 0, width, height);
    CheckGLError("Cube::SetViewport", __FILE__, __LINE__);

//    myGLCamera->SetAspectRatio((float) width / height);
}


/**
 * reset model's position in double-tap
 */
void ModelAssimp::DoubleTapAction() {

//    myGLCamera->SetModelPosition(modelDefaultPosition);
}

/**
 * rotate the model if user scrolls with one finger
 */
void ModelAssimp::ScrollAction(float distanceX, float distanceY, float positionX, float positionY) {

//    myGLCamera->RotateModel(distanceX, distanceY, positionX, positionY);
}

/**
 * pinch-zoom: move the model closer or farther away
 */
void ModelAssimp::ScaleAction(float scaleFactor) {

//    myGLCamera->ScaleModel(scaleFactor);
}

/**
 * two-finger drag: displace the model by changing its x-y coordinates
 */
void ModelAssimp::MoveAction(float distanceX, float distanceY) {

//    myGLCamera->TranslateModel(distanceX, distanceY);
}
