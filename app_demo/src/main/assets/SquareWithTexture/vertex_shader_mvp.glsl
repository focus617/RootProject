#version 300 es
layout (location = 0) in vec3 a_Position;    //顶点位置
layout (location = 1) in vec2 a_TexCoord;    //顶点纹理坐标

//变换矩阵
uniform mat4 u_ModelMatrix;
uniform mat4 u_ViewMatrix;
uniform mat4 u_ProjectionMatrix;

out vec2 v_TexCoord;          //用于传递给片元着色器的变量

void main()
{
    v_TexCoord = a_TexCoord;

    //根据总变换矩阵计算此次绘制此顶点位置
    gl_Position = u_ProjectionMatrix * u_ViewMatrix * u_ModelMatrix * vec4(a_Position, 1.0f);
}