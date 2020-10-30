#version 330

out vec4 out_color;
uniform vec3 color;

void main(void){
	//Set the color to be the color loaded via uniform
	out_color = vec4(color,1);
}