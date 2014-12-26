#version 400 core

in vec3 position;
in vec2 textureCoords;

out vec2 pass_textureCoords;

uniform mat4 transformationMatrix;
uniform int windowWidth;
uniform int windowHeight;

void main(void) {
    pass_textureCoords = textureCoords;
    gl_Position = transformationMatrix * vec4(position.x/windowWidth, position.y/windowHeight, -position.z, 1.0);
}