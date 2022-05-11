// Basic Texture Shader

#type vertex
#version 310 es

layout (location = 0) in vec3 a_Position;    //顶点位置
layout (location = 1) in vec3 a_Normal;      //顶点法线
layout (location = 2) in vec2 a_TexCoords;    //顶点纹理坐标

//变换矩阵
uniform mat4 u_ModelMatrix;
uniform mat4 u_ViewMatrix;
uniform mat4 u_ProjectionMatrix;

void main()
{
    //根据变换矩阵计算此次绘制此顶点位置
    gl_Position = u_ProjectionMatrix * u_ViewMatrix * u_ModelMatrix * vec4(a_Position, 1.0);
}

#type fragment
#version 310 es

#ifdef GL_ES
precision highp float;
#else
precision mediump float;
#endif

out vec4 color;

void main()
{
    color = vec4(0.04, 0.28, 0.26, 1.0);
}
