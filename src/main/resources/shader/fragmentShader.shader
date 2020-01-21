#version 330 core

out vec4 color;

in vec2 outTextCoords;
in vec3 mvVertexNormal;
in vec3 mvVertexPos;


struct Material {
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    int hasTexture;
    float reflectance;
};


uniform sampler2D texture_sampler;
uniform Material material;

vec4 ambientC;
vec4 diffuseC;
vec4 specularC;

void setupColors(Material material, vec2 textCoords) {
    if (material.hasTexture == 1) {
        ambientC = texture(texture_sampler, textCoords);
        diffuseC = ambientC;
        specularC = ambientC;
    }
    else {
        ambientC = material.ambient;
        diffuseC = material.diffuse;
        specularC = material.specular;
    }
}


void main() {
    setupColors(material, outTextCoords);
    color = ambientC;
}
