#version 400 core

in vec2 position;
in vec2 textureCoords;

out vec2 pass_textureCoords;

uniform mat4 transformationMatrix;
uniform int windowWidth;
uniform int windowHeight;
uniform float priority;

void main(void) {
    pass_textureCoords = textureCoords;
    gl_Position = transformationMatrix * vec4(position.x/windowWidth, position.y/windowHeight, -priority, 1.0);
}