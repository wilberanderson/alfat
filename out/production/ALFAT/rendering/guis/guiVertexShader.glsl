#version 140

//In variables
in vec2 position;

//Out variables to fragment shader
out vec2 textureCoords;

//Uniform variables
uniform mat4 transformationMatrix;

void main(void){
	//Calculate the position and texture coords for the fragment shader
	gl_Position = transformationMatrix * vec4(position, 0.0, 1.0);
	textureCoords = vec2((position.x+1.0)/2.0, 1 - (position.y+1.0)/2.0);
}