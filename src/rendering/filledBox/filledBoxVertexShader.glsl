#version 330

in vec2 position;

uniform mat3 transformation;
uniform mat3 zoomTranslateMatrix;
uniform mat2 aspectRatio;

out vec2 fragment_Position;

void main(void){
    vec3 vertexPosition = transformation * vec3(position, 1);
    vertexPosition.x = vertexPosition.x - 1;
    vertexPosition.y = vertexPosition.y - 1;
    vec2 newPosition = aspectRatio * (zoomTranslateMatrix * vertexPosition).xy;
    gl_Position = vec4(newPosition, 1, 1);
    fragment_Position = newPosition;
}