#version 330

in vec2 position;
uniform vec2 mousePosition;
uniform float fontHeight;

void main(void){
    gl_Position = vec4(position.x+mousePosition.x, position.y*fontHeight+mousePosition.y, 1, 1);
}