#version 330

in vec2 position;
uniform vec2 startPosition;
uniform vec2 endPosition;

out vec2 fragment_Position;


void main(void){
    if(position.x == 0 && position.y == 0){
        gl_Position = vec4(startPosition, 1, 1);
        fragment_Position = startPosition;
    }else{
        gl_Position = vec4(endPosition, 1, 1);
        fragment_Position = endPosition;
    }
}