#version 330

in vec2 position;
uniform vec2 startPosition;
uniform vec2 endPosition;
uniform mat3 zoomTranslateMatrix;
uniform mat2 aspectRatio;
out vec2 fragment_Position;


void main(void){
    vec2 out_Position;
    if(position.x == 0 && position.y == 0){
        out_Position = aspectRatio * vec2((zoomTranslateMatrix * vec3(startPosition, 1)).xy);
    }else{
        out_Position = aspectRatio * vec2((zoomTranslateMatrix * vec3(endPosition, 1)).xy);
    }
    fragment_Position = out_Position;
    gl_Position = vec4(out_Position, 1, 1);
}