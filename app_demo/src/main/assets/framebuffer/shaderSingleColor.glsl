// Basic Texture Shader

#type vertex
#version 310 es

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
