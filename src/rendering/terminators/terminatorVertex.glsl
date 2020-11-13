#version 330

in vec2 position;
uniform vec2 startPosition;
uniform vec2 endPosition;
uniform mat3 zoomTranslateMatrix;
uniform mat2 aspectRatio;
uniform mat3 transformationMatrix;
out vec2 fragment_Position;


void main(void){
    vec3 transformedPosition = zoomTranslateMatrix * transformationMatrix * vec3(position, 1);
    vec2 out_Position = aspectRatio * transformedPosition.xy;
    fragment_Position = out_Position;
    gl_Position = vec4(fragment_Position, 1, 1);
}