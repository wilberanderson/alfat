#version 330

in vec2 pass_textureCoords;
in vec2 vertexPosition;

out vec4 out_color;

uniform vec3 color;
uniform sampler2D fontAtlas;

uniform float width;
uniform float edge;

uniform float borderWidth;
uniform float borderEdge;

uniform vec2 offset;

uniform vec3 outlineColor;

uniform vec2 windowPosition;
uniform vec2 windowSize;

uniform float doClipping;

void main(void){
	if (doClipping == 1){
		if (vertexPosition.x < windowPosition.x || vertexPosition.y < windowPosition.y || vertexPosition.x > windowPosition.x+windowSize.x || vertexPosition.y > windowPosition.y+windowSize.y){
			discard;
		}
	}
	float distance = 1.0 - texture(fontAtlas, pass_textureCoords).a;
	float alpha = 1.0 - smoothstep(width, width + edge, distance);
	
	float distance2 = 1.0 - texture(fontAtlas, pass_textureCoords + offset).a;
	float outlineAlpha = 1.0 - smoothstep(borderWidth, borderWidth + borderEdge, distance2);
	
	float overallAlpha = alpha + (1.0 - alpha) * outlineAlpha;
	vec3 overallColor = mix(outlineColor, color, alpha / overallAlpha); 
	out_color = vec4(overallColor, overallAlpha);

}