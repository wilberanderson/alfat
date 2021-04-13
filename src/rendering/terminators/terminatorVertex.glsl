#version 330

in vec2 position;
in vec2 transformation;
in vec3 color;

uniform mat3 zoomTranslateMatrix;
uniform mat2 aspectRatio;
out vec2 fragment_Position;
out vec3 pass_Color;


void main(void){
    vec3 transformedPosition = zoomTranslateMatrix * vec3(position + transformation, 1);
    vec2 out_Position = aspectRatio * transformedPosition.xy;
    fragment_Position = out_Position;
    gl_Position = vec4(fragment_Position, 1, 1);
    pass_Color = color;
}