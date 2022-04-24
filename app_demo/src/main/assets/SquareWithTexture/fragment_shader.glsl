#version 300 es
#ifdef GL_ES
precision highp float;
#else
precision mediump float;
#endif

layout(location = 0) out vec4 color;

//接收从顶点着色器过来的参数
in vec2 v_TexCoord;

uniform sampler2D u_Texture;

void main()
{
    // 下面这行可用于调试，用对应Color(R,G)来确定顶点坐标是否定义错位
    //color = vec4(v_TexCoord, 0.0, 1.0);

    color = texture(u_Texture, v_TexCoord);
}
