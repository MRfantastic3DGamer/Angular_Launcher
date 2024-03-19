#version 100

#ifdef GL_ES
precision mediump float;
#endif
#define MAX_ICONS 100

uniform float u_time;
uniform vec2 u_resolution;
uniform vec2 u_mouse;
uniform vec2 u_interaction;

uniform float u_positions_X[MAX_ICONS];
uniform float u_positions_Y[MAX_ICONS];

vec3 randomColor(int value) {
    // Use a hash function to ensure the same input produces the same output color
    float r = fract(sin(float(value) * 12.9898) * 43758.5453);
    float g = fract(sin(float(value) * 78.233) * 43758.5453);
    float b = fract(sin(float(value) * 45.7269) * 43758.5453);

    return vec3(r, g, b);
}

void main() {
    vec3 color = vec3(0.);
    float dist = distance(u_mouse, gl_FragCoord.xy);
    float t_dist = dist;
    for(int i=0; i<MAX_ICONS && u_positions_X[i] != -10000.; ++i){
        t_dist = distance(vec2(u_positions_X[i], u_positions_Y[i]), gl_FragCoord.xy);
        if (t_dist < dist){
            dist = t_dist;
            color = randomColor(i);
        }
    }

    gl_FragColor = vec4(color, 1.);
}