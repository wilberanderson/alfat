#version 330

in vec2 position;
uniform vec2 startPosition;
uniform vec2 endPosition;
uniform mat3 zoomTranslateMatrix;
out vec2 fragment_Position;


void main(void){
    vec3 out_Position;
    if(position.x == 0 && position.y == 0){
        out_Position = zoomTranslateMatrix * vec3(startPosition, 1);
    }else{
        out_Position = zoomTranslateMatrix * vec3(endPosition, 1);
    }
    fragment_Position = out_Position.xy;
    gl_Position = vec4(out_Position, 1);
}