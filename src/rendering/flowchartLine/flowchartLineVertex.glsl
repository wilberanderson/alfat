#version 330

in vec2 position;
in vec4 endPositions;
in vec3 color;

uniform mat3 zoomTranslateMatrix;
uniform mat2 aspectRatio;
out vec2 fragment_Position;
out vec3 pass_color;

void main(void){
    vec2 out_Position;
    if(position.x == 0 && position.y == 0){
        out_Position = aspectRatio * vec2((zoomTranslateMatrix * vec3(endPositions.xy, 1)).xy);
    }else{
        out_Position = aspectRatio * vec2((zoomTranslateMatrix * vec3(endPositions.zw, 1)).xy);
    }
    fragment_Position = out_Position;
    gl_Position = vec4(out_Position, 1, 1);
    pass_color = color;
}