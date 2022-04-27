// Flat Color Shader

#type vertex
#version 300 es

layout (location = 0) in vec3 a_Position;    //顶点位置
layout (location = 1) in vec2 a_TexCoord;    //顶点纹理坐标

//变换矩阵
uniform mat4 u_ModelMatrix;
uniform mat4 u_ViewMatrix;
uniform mat4 u_ProjectionMatrix;

void main()
{
    //根据总变换矩阵计算此次绘制此顶点位置
    gl_Position = u_ProjectionMatrix * u_ViewMatrix * u_ModelMatrix * vec4(a_Position, 1.0f);
}

#type fragment
#version 300 es
#ifdef GL_ES
precision highp float;
#else
precision mediump float;
#endif

layout(location = 0) out vec4 color;

uniform vec4 u_Color;

void main()
{
    color = u_Color;
}