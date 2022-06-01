#include <jni.h>
#include <cstdio>
#include <cstdlib>
#include <cmath>
#include <string>

#include <unistd.h>

#include <GLES3/gl3.h>
#include <GLES3/gl3ext.h>

/* [New includes and global variables.] */
#include <assimp/cimport.h>
#include <assimp/scene.h>

#include "Core.h"

/* The global Assimp scene object. */
const struct aiScene* scene = NULL;


static const char glVertexShader[] =
        "attribute vec4 vPosition;\n"
        "void main()\n"
        "{\n"
        "  gl_Position = vPosition;\n"
        "}\n";

static const char glFragmentShader[] =
        "precision mediump float;\n"
        "void main()\n"
        "{\n"
        "  gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);\n"
        "}\n";

GLuint loadShader(GLenum shaderType, const char *shaderSource) {
    GLuint shader = glCreateShader(shaderType);
    if (shader) {
        glShaderSource(shader, 1, &shaderSource, NULL);
        glCompileShader(shader);
        GLint compiled = 0;
        glGetShaderiv(shader, GL_COMPILE_STATUS, &compiled);
        if (!compiled) {
            GLint infoLen = 0;
            glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &infoLen);
            if (infoLen) {
                char *buf = (char *) malloc(infoLen);
                if (buf) {
                    glGetShaderInfoLog(shader, infoLen, NULL, buf);
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
                    glGetProgramInfoLog(program, bufLength, NULL, buf);
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
GLuint simpleTriangleProgram;
GLuint vPosition;

bool setupGraphics(int width, int height) {
    simpleTriangleProgram = createProgram(glVertexShader, glFragmentShader);
    if (!simpleTriangleProgram) {
        LOGE ("Could not create program");
        return false;
    }
    vPosition = glGetAttribLocation(simpleTriangleProgram, "vPosition");
    glViewport(0, 0, width, height);

    /* [Load a model into the Open Asset Importer.] */
    std::string sphere = "s 0 0 0 10";
    scene = aiImportFileFromMemory(sphere.c_str(), sphere.length(), 0, ".nff");

    return true;
}

const GLfloat triangleVertices[] = {
        0.0f, 1.0f,
        -1.0f, -1.0f,
        1.0f, -1.0f
};

void renderFrame() {
    glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
    glUseProgram(simpleTriangleProgram);
    glVertexAttribPointer(vPosition, 2, GL_FLOAT, GL_FALSE, 0, triangleVertices);
    glEnableVertexAttribArray(vPosition);
    glDrawArrays(GL_TRIANGLES, 0, 3);
}

/* [Function prototypes] */
#ifdef __cplusplus
extern "C" {
#endif

/* [Function definition] */
JNIEXPORT void JNICALL
Java_com_focus617_nativelib_NativeLib_00024Companion_init(
        JNIEnv *env, jobject thiz, jint width, jint height) {
    setupGraphics(width, height);
}

JNIEXPORT void JNICALL
Java_com_focus617_nativelib_NativeLib_00024Companion_step(
        JNIEnv *env, jobject thiz) {
    renderFrame();
}

JNIEXPORT void JNICALL
Java_com_focus617_nativelib_NativeLib_00024Companion_ndkEmboss(
        JNIEnv *env, jobject thiz, jintArray data, jint width, jint height) {
    int i, index, pp, rr, gg, bb;
    int *tem;
    int x, y;
    double Lx, Ly, Lz, NzLz, az, elev;
    int Nx, Ny, Nz;
    int yl, yr, xl, xr, v;
    int i1, i2, i3, i4, i5, i6, i7, i8;

    // Get input data
    jint *buffer = env->GetIntArrayElements(data, NULL);
    env->ReleaseIntArrayElements(data, buffer, JNI_ABORT);
    tem = (int *) malloc(sizeof(int) * width * height);

    // Populate into the output array as greyscale
    for (i = 0; i < width * height; i++) {
        pp = buffer[i];
        rr = ((pp >> 16) & 0xff);
        gg = ((pp >> 8) & 0xff);
        bb = (pp & 0xff);
        pp = (int) (rr * 0.3 + gg * 0.59 + bb * 0.11);
        tem[i] = pp;
    }

    // Prepare the light source
    az = 30 * 3.14159 / 180;
    elev = 30 * 3.14159 / 180;
    Lx = cos(az) * cos(elev) * 255;
    Ly = sin(az) * cos(elev) * 255;
    Lz = sin(elev) * 255;
    Nz = (6 * 255) / 3;
    NzLz = Nz * Lz;

    // Apply the light source
    for (y = 0; y <= height - 1; y++) {
        for (x = 0; x <= width - 1; x++) {
            index = y * width + x;
            yl = (y - 1 + height) % height;
            xl = (x - 1 + width) % width;
            yr = (y + 1 + height) % height;
            xr = (x + 1 + width) % width;
            i1 = yl * width + xl;
            i2 = yr * width + xr;
            i3 = yr * width + xl;
            i4 = yl * width + xr;
            i5 = y * width + xl;
            i6 = y * width + xr;
            i7 = yr * width + x;
            i8 = yl * width + x;
            Nx = tem[i1] + tem[i5] + tem[i3] - tem[i4] - tem[i6]
                 - tem[i2];
            Ny = tem[i3] + tem[i7] + tem[i2] - tem[i1] - tem[i8]
                 - tem[i4];
            v = (int) (Nx * Lx + Ny * Ly + NzLz);
            if (Nx == 0 && Ny == 0)
                v = (int) Lz;
            else if (v < 0)
                v = 0;
            else
                v = (int) (v / sqrt(Nx * Nx + Ny * Ny + Nz * Nz));
            v = (v > 255) ? 255 : ((v < 0) ? 0 : v);
            buffer[index] = 0xff000000 | (v << 16) | (v << 8) | v;
        }
    }

    free(tem);

    // Update output array
    env->SetIntArrayRegion(data, 0, width * height, buffer);
}

#ifdef __cplusplus
}
#endif
/* [Function definition] */