// Basic Texture Shader

#type vertex
#version 310 es

layout (location = 0) in vec2 a_Position;    //顶点位置
layout (location = 1) in vec2 a_TexCoords;    //顶点纹理坐标

//用于传递给片元着色器的变量
out vec2 v_TexCoords;

void main()
{
    v_TexCoords = a_TexCoords;

    //FrameBuffer本来就是二维
    gl_Position = vec4(a_Position.x, a_Position.y, 0.0f, 1.0f);
}

#type fragment
#version 310 es

#ifdef GL_ES
precision highp float;
#else
precision mediump float;
#endif

//接收从顶点着色器过来的参数
in vec2 v_TexCoords;
out vec4 FragColor;

uniform sampler2D u_screenTexture;
uniform vec4 u_Color;

void main()
{
    FragColor = texture(u_screenTexture, v_TexCoords) * u_Color;
}
