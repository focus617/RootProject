#include <jni.h>
#include <cstdio>
#include <cstdlib>
#include <cmath>
#include <string>

#include <unistd.h>

#include <GLES3/gl3.h>
#include <GLES3/gl3ext.h>

/* [New includes and global variables.] */
#include <vector>

#include <assimp/cimport.h>
#include <assimp/scene.h>

#include "Core.h"
#include "renderer/NativeRenderer.h"
#include "common/MyJNIHelper.h"
#include "math/Matrix.h"

/* The global Assimp scene object. */
const struct aiScene *scene = nullptr;

std::vector<GLfloat> vertices;
std::vector<GLushort> indices;

static const char glVertexShader[] =
        "attribute vec4 vertexPosition;\n"
        "attribute vec3 vertexColour;\n"
        "varying vec3 fragColour;\n"
        "uniform mat4 projection;\n"
        "uniform mat4 modelView;\n"
        "void main()\n"
        "{\n"
        "    gl_Position = projection * modelView * vertexPosition;\n"
        "    fragColour = vertexColour;\n"
        "}\n";

static const char glFragmentShader[] =
        "precision mediump float;\n"
        "varying vec3 fragColour;\n"
        "void main()\n"
        "{\n"
        "    gl_FragColor = vec4(fragColour, 1.0);\n"
        "}\n";

GLuint loadShader(GLenum shaderType, const char *shaderSource) {
    GLuint shader = glCreateShader(shaderType);
    if (shader) {
        glShaderSource(shader, 1, &shaderSource, nullptr);
        glCompileShader(shader);
        GLint compiled = 0;
        glGetShaderiv(shader, GL_COMPILE_STATUS, &compiled);
        if (!compiled) {
            GLint infoLen = 0;
            glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &infoLen);
            if (infoLen) {
                char *buf = (char *) malloc(infoLen);
                if (buf) {
                    glGetShaderInfoLog(shader, infoLen, nullptr, buf);
                    LOGE("Could not Compile Shader %d:\n%s\n", shaderType, buf);
                    free(buf);
                }
                glDeleteShader(shader);
                shader = 0;
            }
        }
    }
    return shader;
}

GLuint createProgram(const char *vertexSource, const char *fragmentSource) {
    GLuint vertexShader = loadShader(GL_VERTEX_SHADER, vertexSource);
    if (!vertexShader) {
        return 0;
    }
    GLuint fragmentShader = loadShader(GL_FRAGMENT_SHADER, fragmentSource);
    if (!fragmentShader) {
        return 0;
    }
    GLuint program = glCreateProgram();
    if (program) {
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glLinkProgram(program);
        GLint linkStatus = GL_FALSE;
        glGetProgramiv(program, GL_LINK_STATUS, &linkStatus);
        if (linkStatus != GL_TRUE) {
            GLint bufLength = 0;
            glGetProgramiv(program, GL_INFO_LOG_LENGTH, &bufLength);
            if (bufLength) {
                char *buf = (char *) malloc(bufLength);
                if (buf) {
                    glGetProgramInfoLog(program, bufLength, nullptr, buf);
                    LOGE("Could not link program:\n%s\n", buf);
                    free(buf);
                }
            }
            glDeleteProgram(program);
            program = 0;
        }
    }
    return program;
}

/* [setupGraphics] */
GLuint program;
GLuint vertexLocation;
GLuint vertexColourLocation;
GLuint projectionLocation;
GLuint modelViewLocation;

float projectionMatrix[16];
float modelViewMatrix[16];
float angle = 0;

bool setupGraphics(int width, int height) {
    program = createProgram(glVertexShader, glFragmentShader);
    if (!program) {
        LOGE ("Could not create program");
        return false;
    }
    vertexLocation = glGetAttribLocation(program, "vertexPosition");
    vertexColourLocation = glGetAttribLocation(program, "vertexColour");
    projectionLocation = glGetUniformLocation(program, "projection");
    modelViewLocation = glGetUniformLocation(program, "modelView");

    /* Setup the perspective */
    matrixPerspective(projectionMatrix, 45, (float) width / (float) height, 0.1f, 100);
    glEnable(GL_DEPTH_TEST);

    /* [Load a model into the Open Asset Importer.] */
    std::string sphere = "s 0 0 0 10";
    scene = aiImportFileFromMemory(sphere.c_str(), sphere.length(), 0, ".nff");
    if (!scene) {
        LOGE("Open Asset Importer could not load scene. \n");
        return false;
    }

    /* [Load a model into the Open Asset Importer.] */

    /* [Accumulate the model vertices and indices.] */
    int vertices_accumulation = 0;
    /* Go through each mesh in the scene. */
    for (int i = 0; i < scene->mNumMeshes; i++) {
        /* Add all the vertices in the mesh to our array. */
        for (int j = 0; j < scene->mMeshes[i]->mNumVertices; j++) {
            const aiVector3D &vector = scene->mMeshes[i]->mVertices[j];
            vertices.push_back(vector.x);
            vertices.push_back(vector.y);
            vertices.push_back(vector.z);
        }
        /*
         * Add all the indices in the mesh to our array.
         * Indices are listed in the Open Asset importer relative to the mesh they are in.
         * Because we are adding all vertices from all meshes to one array we must add an offset
         * to the indices to correct for this.
         */
        for (unsigned int j = 0; j < scene->mMeshes[i]->mNumFaces; j++) {
            const aiFace &face = scene->mMeshes[i]->mFaces[j];
            indices.push_back(face.mIndices[0] + vertices_accumulation);
            indices.push_back(face.mIndices[1] + vertices_accumulation);
            indices.push_back(face.mIndices[2] + vertices_accumulation);
        }
        /* Keep track of number of vertices loaded so far to use as an offset for the indices. */
        vertices_accumulation += scene->mMeshes[i]->mNumVertices;
    }
    /* [Accumulate the model vertices and indices.] */

    return true;
}

void renderFrame() {
    matrixIdentityFunction(modelViewMatrix);
    matrixRotateX(modelViewMatrix, angle);
    matrixRotateY(modelViewMatrix, angle);
    matrixTranslate(modelViewMatrix, 0.0f, 0.0f, -10.0f);

    /* [Pass the the model vertices and indices to OpenGL ES.] */
    glUseProgram(program);
    /* Use the vertex data loaded from the Open Asset Importer. */
    glVertexAttribPointer(vertexLocation, 3, GL_FLOAT, GL_FALSE, 0, &vertices[0]);
    glEnableVertexAttribArray(vertexLocation);
    /* We're using vertices as the colour data here for simplicity. */
    glVertexAttribPointer(vertexColourLocation, 3, GL_FLOAT, GL_FALSE, 0, &vertices[0]);
    glEnableVertexAttribArray(vertexColourLocation);
    glUniformMatrix4fv(projectionLocation, 1, GL_FALSE, projectionMatrix);
    glUniformMatrix4fv(modelViewLocation, 1, GL_FALSE, modelViewMatrix);
    /* Use the index data loaded from the Open Asset Importer. */
    glDrawElements(GL_TRIANGLES, indices.size(), GL_UNSIGNED_SHORT, &indices[0]);
    /* [Pass the the model vertices and indices to OpenGL ES.] */

    angle += 1;
    if (angle > 360) {
        angle -= 360;
    }
}


