#version 330

in vec2 fragment_Position;

out vec4 out_color;

uniform vec3 color;
uniform vec2 windowPosition;
uniform vec2 windowSize;


void main(void){
	if(fragment_Position.x < windowPosition.x || fragment_Position.y < windowPosition.y || fragment_Position.x > windowPosition.x+windowSize.x || fragment_Position.y > windowPosition.y+windowSize.y){
		discard;
	}
	//Set the color to be the color loaded via uniform
	out_color = vec4(color, 1);
}