#version 330

in vec2 pass_textureCoords;
in vec2 vertexPosition;

out vec4 out_color;

uniform vec3 color;
uniform sampler2D fontAtlas;

uniform float width;
uniform float edge;

uniform vec2 windowPosition;
uniform vec2 windowSize;

uniform float doClipping;

void main(void){
	if (vertexPosition.x < windowPosition.x || vertexPosition.y < windowPosition.y || vertexPosition.x > windowPosition.x+windowSize.x || vertexPosition.y > windowPosition.y+windowSize.y){
		discard;
	}
	float distance = 1.0 - texture(fontAtlas, pass_textureCoords).a;
	float alpha = 1.0 - smoothstep(width, width + edge, distance);

	out_color = vec4(color, alpha);
}