#version 400 core

in vec3 position;
in vec2 textureCoords;

out vec2 pass_textureCoords;

uniform mat4 transformationMatrix;
uniform int windowWidth;
uniform int windowHeight;

void main(void) {
    pass_textureCoords = textureCoords;
    
    float x = (2f/1280f)*position.x;
    float y = (2f/720f)*position.y;

    gl_Position = transformationMatrix * vec4(x, y, -position.z, 1.0);
}