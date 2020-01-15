#version 330 core

out vec4 color;
in vec2 outTextCoords;

uniform sampler2D texture_sampler;

void main() {
    color = texture(texture_sampler, outTextCoords);
}
