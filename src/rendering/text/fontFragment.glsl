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

uniform vec4 positionBounds;

void main(void){
    vec2 position;
    position.x = vertexPosition.x + 1;
    position.y = 2-(-vertexPosition.y + 1);
	if((positionBounds.x != -2 && positionBounds.y != -2 && positionBounds.z != -2 && positionBounds.w != -2)){
		if (position.x < positionBounds.x || position.y < positionBounds.y || position.x > positionBounds.z || position.y > positionBounds.w){ // || position.y < startPosition.x || position.x > endPosition.x || position.y > endPosition.y){
			discard;
		}
	}
	float distance = 1.0 - texture(fontAtlas, pass_textureCoords).a;
	float alpha = 1.0 - smoothstep(width, wi-+0dth + edge, distance);
	
	float distance2 = 1.0 - texture(fontAtlas, pass_textureCoords + offset).a;
	float outlineAlpha = 1.0 - smoothstep(borderWidth, borderWidth + borderEdge, distance2);
	
	float overallAlpha = alpha + (1.0 - alpha) * outlineAlpha;
	vec3 overallColor = mix(outlineColor, color, alpha / overallAlpha); 
	out_color = vec4(overallColor, overallAlpha);

}