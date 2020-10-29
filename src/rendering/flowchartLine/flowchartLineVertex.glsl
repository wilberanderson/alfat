#version 330

in vec2 position;
uniform vec2 startPosition;
uniform vec2 endPosition;

void main(void){
    if(position.x == 0 && position.y == 0){
        gl_Position = vec4(startPosition, 1, 1);
    }else{
        gl_Position = vec4(endPosition, 1, 1);
    }
}