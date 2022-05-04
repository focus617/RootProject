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
out vec3 v_Position;

void main()
{
    v_Position = a_Position;
    // Make sure to convert from the right-handed coordinate system of the
    // world to the left-handed coordinate system of the cube map, otherwise,
    // our cube map will still work but everything will be flipped.
    v_Position.z = -v_Position.z;

    //根据变换矩阵计算此次绘制此顶点位置
    gl_Position = u_ProjectionMatrix * u_ViewMatrix * u_ModelMatrix * vec4(a_Position, 1.0);
    gl_Position = gl_Position.xyww;
}

#type fragment
#version 300 es
#ifdef GL_ES
precision highp float;
#else
precision mediump float;
#endif

//接收从顶点着色器过来的参数
in vec3 v_Position;

uniform samplerCube u_TextureUnit;

out vec4 FragColor;

void main() {
    // 下面这行可用于调试，用对应Color(R,G)来确定顶点坐标是否定义错位
    //FragColor = vec4(v_TexCoords, 0.0, 1.0);

    FragColor = texture(u_TextureUnit, v_Position);
}
