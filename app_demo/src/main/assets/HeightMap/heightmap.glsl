// HeightMap Shader

#type vertex
#version 300 es

layout (location = 0) in vec3 a_Position;    //顶点位置
layout (location = 1) in vec3 a_Normal;      //顶点法线
layout (location = 2) in vec2 a_TexCoords;    //顶点纹理坐标

//变换矩阵
uniform mat4 u_ModelMatrix;
uniform mat4 u_ViewMatrix;
uniform mat4 u_ProjectionMatrix;

uniform vec3 u_VectorToLight;

//用于传递给片元着色器的变量
out vec3 v_Color;
out vec2 v_TextureCoordinates;
out float v_Ratio;

void main()
{
    v_TextureCoordinates = a_TexCoords;
    v_Ratio = a_Position.y;

    v_Color = mix(vec3(0.180f, 0.467f, 0.153f),    // A dark green
                  vec3(0.660f, 0.670f, 0.680f),    // A stony gray
                  a_Position.y);

    vec3 scaledNormal = a_Normal;
    scaledNormal.y *= 50.0;
    scaledNormal = normalize(scaledNormal);

    float diffuse = max(dot(scaledNormal, u_VectorToLight), 0.0);
    v_Color *= diffuse;

    float ambient = 0.1;
    v_Color += ambient;

    //根据变换矩阵计算此次绘制此顶点位置
    gl_Position = u_ProjectionMatrix * u_ViewMatrix * u_ModelMatrix * vec4(a_Position, 1.0);
}

#type fragment
#version 300 es
#ifdef GL_ES
precision highp float;
#else
precision mediump float;
#endif

//接收从顶点着色器过来的参数
in vec3 v_Color;
in vec2 v_TextureCoordinates;
in float v_Ratio;

uniform sampler2D u_TextureUnit1;
uniform sampler2D u_TextureUnit2;

out vec4 FragColor;

void main()
{
    FragColor = texture(u_TextureUnit1, v_TextureCoordinates) * (1.0 - v_Ratio);
    // Divide the texture coordinates by 2 to make the stone texture repeat half as often.
    FragColor += texture(u_TextureUnit2, v_TextureCoordinates / 2.0) * v_Ratio;

    FragColor *= vec4(v_Color, 1.0f);
}
