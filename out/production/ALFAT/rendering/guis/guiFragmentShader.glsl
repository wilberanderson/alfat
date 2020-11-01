#version 140

//In variables from vertex shader
in vec2 textureCoords;

//Out variables to screen
out vec4 out_Color;

//Uniform variables
uniform sampler2D guiTexture;
uniform float highlighted;

void main(void){
	//Texture with the guiTexture
	out_Color = texture(guiTexture,vec2(textureCoords.x, textureCoords.y));
	if(highlighted > 0.5){
		out_Color = mix(out_Color, vec4(1.0), 0.5);
	}
}