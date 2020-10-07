#version 330

in vec2 position;
in vec2 textureCoords;

out vec2 pass_textureCoords;
out vec2 vertexPosition;
uniform vec2 translation;

void main(void){

	gl_Position = vec4(position.x + translation.x + 1, position.y + translation.y - 1, 0.0, 1.0);
	pass_textureCoords = textureCoords;
    vertexPosition = gl_Position.xy;
}