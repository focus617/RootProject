#version 300 es
#ifdef GL_ES
precision highp float;
#else
precision mediump float;
#endif

layout(location = 0) out vec4 color;

//接收从顶点着色器过来的参数
in vec3 v_Position;
in vec4 v_Color;

void main()
{
    color = vec4(v_Position * 0.5 + 0.5, 1.0);
    color = v_Color;
}
