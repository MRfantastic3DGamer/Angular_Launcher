#version 100
#ifdef GL_FRAGMENT_PRECISION_HIGH
precision highp float;
#else
precision mediump float;
#endif
#define MAX_ICONS 100
uniform vec2 GroupsPositions[MAX_ICONS];
uniform int SelectedGroupIndex;
uniform int Frame;
uniform float GroupZoneStartRadios;
uniform float GroupZoneEndRadios;

#define adjust vec2(0.0, -40.0)
#define sr 100.0
#define spacing 10.0
#define dotSize 10.0

void main() {

    float rep = 5.0;

    vec2 center = GroupsPositions[SelectedGroupIndex] + adjust;
    float selectionDist = distance(gl_FragCoord.xy, center);
    float isInGroupZone;
    if (selectionDist > GroupZoneStartRadios - 100.0 && selectionDist < GroupZoneEndRadios + 100.0) {
        // Smooth interpolation from 0.0 to 1.0 based on the distance to the zone boundary
        isInGroupZone = smoothstep(GroupZoneStartRadios - 100.0, GroupZoneStartRadios, selectionDist);
        isInGroupZone *= 1.0 - smoothstep(GroupZoneEndRadios, GroupZoneEndRadios + 100.0, selectionDist);
    } else {
        isInGroupZone = 0.0; // Outside the group zone
    }
    float tr = (1.0 - smoothstep(0.0, 1.0, sin(float(Frame) * 0.01 - selectionDist * 0.01)*1.2)) * isInGroupZone;

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

    // Rotate the UV coordinates
    vec2 uvR = rotationMatrixR * uv + vec2(float(Frame) * 0.01, float(Frame) * 0.02);
    vec2 uvG = rotationMatrixG * uv + vec2(float(Frame) * 0.02, float(Frame) * 0.01);
    vec2 uvB = rotationMatrixB * uv + vec2(float(Frame) * 0.00, float(Frame) * 0.01);

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

    if (distanceToCenterR < dotSize * tr) { color.r = 1.0; }
    if (distanceToCenterG < dotSize * tr) { color.g = 1.0; }
    if (distanceToCenterB < dotSize * tr) { color.b = 1.0; }

    gl_FragColor = vec4(color, 1.0);
}