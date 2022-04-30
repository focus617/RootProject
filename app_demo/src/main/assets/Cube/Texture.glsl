// Basic Texture Shader

#type vertex
#version 300 es

layout (location = 0) in vec3 a_Position;    //顶点位置
layout (location = 1) in vec3 a_Normal;      //顶点法线
layout (location = 2) in vec2 a_TexCoords;    //顶点纹理坐标

//变换矩阵
uniform mat4 u_ModelMatrix;
uniform mat4 u_ViewMatrix;
uniform mat4 u_ProjectionMatrix;

//用于传递给片元着色器的变量
out vec3 v_worldSpacePos;
out vec3 v_worldSpaceViewPos;
out vec3 v_Normal;
out vec2 v_TexCoords;

void main()
{
    gl_Position = u_ProjectionMatrix * u_ViewMatrix * u_ModelMatrix * vec4(a_Position, 1.0);
}

#type fragment
#version 300 es
#ifdef GL_ES
precision highp float;
#else
precision mediump float;
#endif

out vec4 FragColor;
uniform vec4 outColor;

void main() {
  FragColor = outColor;
}
