#version 330

in vec2 fragment_Position;

out vec4 out_color;

in vec3 pass_color;
uniform vec2 windowPosition;
uniform vec2 windowSize;
//uniform float doClipping;

void main(void){
	//if(doClipping == 1){
		if (fragment_Position.x < windowPosition.x || fragment_Position.y < windowPosition.y || fragment_Position.x > windowPosition.x+windowSize.x || fragment_Position.y > windowPosition.y+windowSize.y){
			discard;
		}
	//}
	//Set the color to be the color loaded via uniform
	out_color = vec4(pass_color, 0.25);
}