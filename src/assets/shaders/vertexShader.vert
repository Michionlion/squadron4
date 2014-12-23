#version 400 core

in vec3 position;
in vec2 textureCoords;

out vec2 pass_textureCoords;

uniform mat4 transformationMatrix;

void main(void) {
    pass_textureCoords = textureCoords;
    gl_Position = vec4(position, 1.0);
}