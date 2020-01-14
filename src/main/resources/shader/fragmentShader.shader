#version 330 core

out vec4 color;
in vec3 exColor;

void main() {
    color = vec4(exColor, 1.0);
}
