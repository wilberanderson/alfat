#version 330

in vec2 position;
in vec2 textureCoords;

out vec2 pass_textureCoords;
out vec2 vertexPosition;

uniform vec2 translation;
uniform mat3 zoomTranslateMatrix;
uniform mat2 aspectRatio;

void main(void){
    vec2 out_Position = aspectRatio * vec2((zoomTranslateMatrix * vec3(position.x + translation.x + 1, position.y + translation.y - 1, 1)).xy);
	gl_Position = vec4(out_Position.xy, 1.0, 1.0);
	pass_textureCoords = textureCoords;
    vertexPosition = gl_Position.xy;
}