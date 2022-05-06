// Earth Shader

#type vertex
#version 300 es

layout (location = 0) in vec3 a_Position;           //顶点位置
layout (location = 1) in vec3 a_Normal;             //顶点法线
layout (location = 2) in vec2 a_TextureCoordinates;

//变换矩阵
uniform mat4 u_ModelMatrix;
uniform mat4 u_ViewMatrix;
uniform mat4 u_ProjectionMatrix;

uniform vec3 u_ViewPos;

out vec3 v_worldSpacePos;
out vec3 v_worldSpaceViewPos;
out vec3 v_Normal;
out vec2 v_TexCoords;

void main()
{
    //根据总变换矩阵计算此次绘制此顶点位置
    gl_Position = u_ProjectionMatrix * u_ViewMatrix * u_ModelMatrix * vec4(a_Position, 1.0);

    // 将着色点和摄像机转换到世界坐标空间
    v_worldSpacePos = vec3(u_ModelMatrix * vec4(a_Position, 1.0));

    v_worldSpaceViewPos = u_ViewPos;

    v_Normal = a_Normal;

    //将顶点的纹理坐标传给片元着色器
    v_TexCoords = a_TextureCoordinates;
}

#type fragment
#version 300 es
#ifdef GL_ES
precision highp float;
#else
precision mediump float;
#endif

uniform sampler2D u_TextureUnit;

in vec3 v_worldSpacePos;
in vec3 v_worldSpaceViewPos;
in vec3 v_Normal;
in vec2 v_TexCoords;

out vec4 gl_FragColor;//输出到的片元颜色

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

    vec3 ambient = getAmbientLighting();
    vec3 diffuse = getDiffuseLighting();
    vec3 specular = getSpecularLighting();

    //纹理采样颜色值
    vec3 moonColor = vec3(texture(u_TextureUnit, v_TexCoords));

    vec3 result = ambient*moonColor + diffuse*moonColor + specular*moonColor;

    //将计算出的颜色传递给管线
    gl_FragColor = vec4(result, 1.0);
}

// 环境光
vec3 getAmbientLighting()
{
    vec3 ambient = light.ambient;

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

    vec3 diffuse = light.diffuse * diff;

    return diffuse;
}

// 镜面光
vec3 getSpecularLighting()
{
    float  shininess = 100.0;// 高光的反光度

    float spec = pow(max(dot(viewDir, reflectDir), 0.0), shininess);
    vec3 specular = light.specular * spec;

    return specular;
}
