#version 330

in vec2 position;
uniform vec2 mousePosition;
uniform float fontHeight;
uniform mat2 aspectRatio;

out vec2 fragment_Position;

void main(void){
    fragment_Position = aspectRatio*vec2(position.x+mousePosition.x, position.y*fontHeight+mousePosition.y);
    gl_Position = vec4(fragment_Position, 1, 1);
}