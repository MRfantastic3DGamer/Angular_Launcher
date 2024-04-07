#version 100

#ifdef GL_ES
precision mediump float;
#endif

#define MAX_ICONS 100

uniform vec2 TouchPosition;
uniform vec2 IconsPositions[MAX_ICONS];
uniform int IconsCount;

uniform vec4 GroupsPositioning;
uniform int SelectedGroupIndex;
uniform int GroupsCount;

void main() {

    float value = 1./distance(gl_FragCoord.xy, TouchPosition) * 1.2;
    vec2 t_pos = vec2(0., 0.);
    float t_dist = 100000.;

    for(int i=0;i<IconsCount;++i){
        t_pos = IconsPositions[i];
        t_dist = min(t_dist, distance(gl_FragCoord.xy, t_pos));
    }
    for(int i=0;i<GroupsCount;++i){
        t_pos = vec2(
            GroupsPositioning.x - GroupsPositioning.w * (1 - clamp(abs(i - SelectedGroupIndex), 0, 1)),
            GroupsPositioning.y + float(i) * GroupsPositioning.z
        );
        t_dist = min(t_dist, distance(gl_FragCoord.xy, t_pos));
    }
    value += 1./t_dist;

    float radius = 1./50.0;

    float insideCircle = step(value, radius);

    vec4 color = insideCircle * vec4(0.0, 0.0, 0.0, 0.0);
    color += (1.0 - insideCircle) * vec4(1.0, 1.0, 1.0, 1.0);

    gl_FragColor = color;
}