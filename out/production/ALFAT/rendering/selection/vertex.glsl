#version 330

in vec2 position;

uniform mat3 transformation;

void main(void){
    vec3 vertexPosition = vec3(position, 1);
    vertexPosition = transformation*vertexPosition;
    gl_Position = vec4(vertexPosition.x-1, vertexPosition.y-1, 1, 1);
}