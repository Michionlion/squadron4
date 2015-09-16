#version 130

in vec3 position;
in vec2 textureCoords;

out vec2 pass_textureCoords;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform int windowWidth;
uniform int windowHeight;

void main(void) {
    pass_textureCoords = textureCoords;
    
    float x = position.x;
    float y = position.y;

    gl_Position = projectionMatrix * transformationMatrix * vec4(x, y, -position.z, 1.0);
}