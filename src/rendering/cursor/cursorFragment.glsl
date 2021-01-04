#version 330

in vec2 fragment_Position;

out vec4 out_color;
uniform vec3 color;
uniform float isVisible;


void main(void){
	if(isVisible == 0){
		discard;
	}

	//Set the color to be the color loaded via uniform
	out_color = vec4(color,1);
}