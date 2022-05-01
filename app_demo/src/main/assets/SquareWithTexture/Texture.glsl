// Basic Texture Shader

#type vertex
#version 310 es

layout (location = 0) in vec3 a_Position;    //顶点位置
layout (location = 1) in vec4 a_Color;       //顶点颜色
layout (location = 2) in vec2 a_TexCoord;    //顶点纹理坐标
layout (location = 3) in float a_TexIndex;
layout (location = 4) in float a_TilingFactor;

//变换矩阵
uniform mat4 u_ModelMatrix;
uniform mat4 u_ViewMatrix;
uniform mat4 u_ProjectionMatrix;

//用于传递给片元着色器的变量
out vec4 v_Color;
out vec2 v_TexCoord;
out float v_TexIndex;
out float v_TilingFactor;

void main()
{
    v_Color = a_Color;
    v_TexCoord = a_TexCoord;
    v_TexIndex = a_TexIndex;
    v_TilingFactor = a_TilingFactor;

    //根据总变换矩阵计算此次绘制此顶点位置
    gl_Position = u_ProjectionMatrix * u_ViewMatrix * u_ModelMatrix * vec4(a_Position, 1.0f);
}

#type fragment
#version 310 es
#ifdef GL_ES
precision highp float;
#else
precision mediump float;
#endif

layout(location = 0) out vec4 color;

//接收从顶点着色器过来的参数
in vec4 v_Color;
in vec2 v_TexCoord;
in float v_TexIndex;
in float v_TilingFactor;

uniform sampler2D u_Textures[32];

void main()
{
    // 下面这行可用于调试，用对应Color(R,G)来确定顶点坐标是否定义错位
    //color = vec4(v_TexCoord, 0.0, 1.0);
    color = texture(u_Textures[int(v_TexIndex)], v_TexCoord * v_TilingFactor) * v_Color;
}
