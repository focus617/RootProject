// Basic Texture Shader

#type vertex
#version 300 es

layout (location = 0) in vec3 a_Position;    //顶点位置
layout (location = 1) in vec3 a_Normal;      //顶点法线
layout (location = 2) in vec2 a_TexCoords;   //顶点纹理坐标

//变换矩阵
uniform mat4 u_ModelMatrix;
uniform mat4 u_ViewMatrix;
uniform mat4 u_ProjectionMatrix;

uniform vec3 u_ViewPos;

//用于传递给片元着色器的变量
out vec3 v_Normal;
out vec2 v_TexCoords;
out vec3 v_worldSpacePos;
out vec3 v_worldSpaceViewPos;

void main()
{
    v_Normal = a_Normal;
    v_TexCoords = a_TexCoords;

    //根据变换矩阵计算此次绘制此顶点位置
    gl_Position = u_ProjectionMatrix * u_ViewMatrix * u_ModelMatrix * vec4(a_Position, 1.0);

    // 将着色点和摄像机转换到世界坐标空间
    v_worldSpacePos = vec3(u_ModelMatrix * vec4(a_Position, 1.0));
    v_worldSpaceViewPos = u_ViewPos;
}

#type fragment
#version 300 es
#ifdef GL_ES
precision highp float;
#else
precision mediump float;
#endif

//接收从顶点着色器过来的参数
in vec3 v_Normal;
in vec2 v_TexCoords;
in vec3 v_worldSpacePos;
in vec3 v_worldSpaceViewPos;

out vec4 FragColor;

struct Material {
    sampler2D diffuse;  // 漫反射贴图
    vec3 specular;      // 镜面强度(Specular Intensity)
    float shininess;    // 高光的反光度
};

uniform Material material;

struct Light {
    vec3 position;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
// 衰减参数
    float constant;
    float linear;
    float quadratic;
};

uniform Light light;

vec3 norm;
vec3 lightDir;
float lightDistance;

vec3 viewDir;
vec3 reflectDir;

vec3 getAmbientLighting();
vec3 getDiffuseLighting();
vec3 getSpecularLighting();

void main()
{
    // 在世界坐标空间中计算 着色点到点光源的距离 和 光照方向
    lightDir = vec3(light.position - v_worldSpacePos);
    lightDistance = length(lightDir);

    // 把法线和最终的方向向量都进行标准化
    norm = normalize(v_Normal);
    lightDir = normalize(lightDir);

    // 计算视线方向向量，和对应的沿着法线轴的反射向量
    viewDir = normalize(v_worldSpaceViewPos - v_worldSpacePos);
    reflectDir = reflect(-lightDir, norm);

    vec3 result = getAmbientLighting();
    result += getDiffuseLighting();
    result += getSpecularLighting();

    FragColor = vec4(result, 1.0);
}


// 环境光
vec3 getAmbientLighting()
{
    //vec3 ambient = light.ambient * material.ambient;
    vec3 ambient = light.ambient * vec3(texture(material.diffuse, v_TexCoords));

    return ambient;
}

// 漫反射
vec3 getDiffuseLighting()
{
    float adjustParam = 5.0;
    float cosine = max(dot(norm, lightDir), 0.0);
    float attenuation = 1.0 / (light.constant + light.linear * lightDistance +
    light.quadratic * (pow(lightDistance, 2.0)));
    float diff = cosine * adjustParam * attenuation;

    //vec3 diffuse = light.diffuse * diff * material.diffuse;
    vec3 diffuse = light.diffuse * diff * vec3(texture(material.diffuse, v_TexCoords));

    return diffuse;
}

// 镜面光
vec3 getSpecularLighting()
{
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    vec3 specular = light.specular * (spec * material.specular);

    return specular;
}

