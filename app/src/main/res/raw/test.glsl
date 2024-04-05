#version 100
#ifdef GL_FRAGMENT_PRECISION_HIGH
precision highp float;
#else
precision mediump float;
#endif

uniform vec2 GroupsPositions[50];
uniform float GroupZoneStartRadios;
uniform float GroupZoneEndRadios;
uniform int GroupsCount;
uniform int SelectedGroupIndex;

uniform int IconsCount;
uniform int SelectedIconIndex;
uniform vec2 IconsPositions[100];

uniform int Frame;

#define sr 100.0
#define adjust vec2(0.0, -40.0)

#define spacing 10.0
#define dotSize 10.0
vec3 triPolka(float mask){
    float rep = 5.0;
    vec2 uv = (gl_FragCoord.xy / rep);

    // Define the angle of rotation in radians
    float angleR = 1.0;
    float angleG = 2.0;
    float angleB = 3.0;
    // Calculate sine and cosine of the angle
    float s = sin(angleR);
    float c = cos(angleR);
    mat2 rotationMatrixR = mat2(c, -s, s, c);
    s = sin(angleG);
    c = cos(angleG);
    mat2 rotationMatrixG = mat2(c, -s, s, c);
    s = sin(angleB);
    c = cos(angleB);
    mat2 rotationMatrixB = mat2(c, -s, s, c);

    // Rotate and move the UV coordinates
    vec2 uvR = rotationMatrixR * uv + vec2(float(Frame) * 0.10   , float(Frame) * 0.02   );
    vec2 uvG = rotationMatrixG * uv + vec2(float(Frame) * (-0.12), float(Frame) * (-0.01));
    vec2 uvB = rotationMatrixB * uv + vec2(float(Frame) * 0.07   , float(Frame) * 0.1    );

    vec2 gridCellPosR = floor(uvR / spacing) * spacing;
    vec2 gridCellPosG = floor(uvG / spacing) * spacing;
    vec2 gridCellPosB = floor(uvB / spacing) * spacing;

    vec2 dotCenterR = gridCellPosR + 0.5 * spacing;
    vec2 dotCenterG = gridCellPosG + 0.5 * spacing;
    vec2 dotCenterB = gridCellPosB + 0.5 * spacing;

    float distanceToCenterR = distance(uvR, dotCenterR);
    float distanceToCenterG = distance(uvG, dotCenterG);
    float distanceToCenterB = distance(uvB, dotCenterB);

    vec3 color = vec3(0.0);

    if (distanceToCenterR < dotSize * mask) { color.r = 1.0; }
    if (distanceToCenterG < dotSize * mask) { color.g = 1.0; }
    if (distanceToCenterB < dotSize * mask) { color.b = 1.0; }

    return color;
}

void main() {

    vec2 center = GroupsPositions[SelectedGroupIndex] + adjust;
    float selectionDist = distance(gl_FragCoord.xy, center);
    float isInGroupZone;
    if (selectionDist > GroupZoneStartRadios - 300.0 && selectionDist < GroupZoneEndRadios + 300.0) {
        // Smooth interpolation from 0.0 to 1.0 based on the distance to the zone boundary
        isInGroupZone = smoothstep(-200.0, -100.0, selectionDist - GroupZoneStartRadios);
        isInGroupZone *= 1.0 - smoothstep(100.0, 200.0, selectionDist - GroupZoneEndRadios);
    } else {
        isInGroupZone = 0.0; // Outside the group zone
    }
    float wave = (1.0 - smoothstep(0.0, 1.0, sin(float(Frame) * 0.01 - selectionDist * 0.01)*1.2)) * isInGroupZone;
    float sliderH = smoothstep(-100.0, 0.0, 1000.0 - 130.0 - gl_FragCoord.x);
    float sliderV = smoothstep(-100.0, 0.0, GroupsPositions[0].y + 50.0 - gl_FragCoord.y) * smoothstep(-100.0, 0.0, gl_FragCoord.y - GroupsPositions[GroupsCount-1].y + 50.0);
    float slider = (1.0 - sliderH) * sliderV;

    float iconCloseness = 1000000.0;
    for(int i=0; i<IconsCount; ++i){
        iconCloseness = min(iconCloseness, distance(gl_FragCoord.xy, IconsPositions[i]))/100.0;
    }

    float mask = clamp(0.0, wave + slider, 1.0);
    if(SelectedGroupIndex == -1){ mask = 0.0; }

    gl_FragColor = vec4(vec3(iconCloseness), 1.0);
}