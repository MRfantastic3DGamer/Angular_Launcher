package com.dhruv.angular_launcher.accessible_screen.components.glsl_art

import com.dhruv.angular_launcher.core.database.room.models.ThemeData
import com.dhruv.angular_launcher.core.resources.AllResources
import com.dhruv.angular_launcher.core.resources.ShaderData

val ThemesFor_GettingBasicDataToDrawStuff = listOf(
    ThemeData(

    )
)

val ShadersFor_GettingBasicDataToDrawStuff = listOf(

    ShaderData(
        name = "test",
        resourcesAsked = setOf(
            AllResources.GroupsPositions.name,
            AllResources.SelectedGroupIndex.name,
            AllResources.GroupZoneStartRadios.name,
            AllResources.GroupZoneEndRadios.name,
            AllResources.IconsCount.name,
            AllResources.IconsPositions.name,
            AllResources.SelectedIconIndex.name,
            AllResources.GroupZoneEndRadios.name,
            AllResources.Frame.name,
        ),
        textures = emptyMap(),
        code =
        """
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
""",
    ),

    ShaderData(
        name = "track cursor",
        resourcesAsked = setOf(
            AllResources.TouchPosition.name,
            AllResources.TouchStartPositions.name,
        ),
        textures = emptyMap(),
        code =
        """
#version 100

#ifdef GL_ES
precision mediump float;
#endif

#define MAX_ICONS 100

uniform vec2 u_resolution;
uniform vec2 TouchStartPositions;
uniform vec2 TouchPosition;

void main() {
    // Calculate the distance from the current fragment to the touch position
    float distToTouch = distance(gl_FragCoord.xy, TouchPosition);

    // Calculate the distance from the current fragment to the initial touch position
    float distToStart = distance(gl_FragCoord.xy, TouchStartPositions);

    // Define the radius of the circles
    float circleRadius = 25.0; // You can adjust the size as needed

    // Define colors
    vec4 yellowColor = vec4(1.0, 1.0, 0.0, 1.0); // Yellow
    vec4 redColor = vec4(1.0, 0.0, 0.0, 1.0);    // Red
    vec4 blackColor = vec4(0.0, 0.0, 0.0, 1.0);   // Black

    // Check if the fragment is inside the circle around TouchPosition
    if (distToTouch <= circleRadius) {
        // Inside the circle, so draw yellow
        gl_FragColor = yellowColor;
    }
    // Check if the fragment is inside the circle around TouchStartPositions
    else if (distToStart <= circleRadius) {
        gl_FragColor = redColor;
    }
    else {
        gl_FragColor = blackColor;
    }
}
"""
    ),


    ShaderData(
        name = "blob apps",
        resourcesAsked = setOf(
            AllResources.TouchPosition.name,
            AllResources.IconsPositions.name,
            AllResources.IconsCount.name,
        ),
        textures = emptyMap(),
        code =
"""
#version 100

#ifdef GL_ES
precision mediump float;
#endif

#define MAX_ICONS 100

uniform vec2 u_resolution;
uniform vec2 TouchPosition;
uniform vec2 IconsPositions[MAX_ICONS];
uniform int IconsCount;

void main() {
    
    float value = 1./distance(gl_FragCoord.xy, TouchPosition) * 1.2;
    vec2 t_pos = vec2(0., 0.);
    float t_dist = 100000.;

    for(int i=0;i<IconsCount;++i)
    {
        t_pos = IconsPositions[i];
        t_dist = min(t_dist, distance(gl_FragCoord.xy, t_pos));
    }
    value += 1./t_dist;

    float radius = 1./50.0;

    float insideCircle = step(value, radius);

    vec4 color = insideCircle * vec4(0.0, 0.0, 0.0, 0.0);
    color += (1.0 - insideCircle) * vec4(1.0, 1.0, 1.0, 1.0);

    gl_FragColor = color;
}
"""
    ),


    ShaderData(
        name = "group zone",
        resourcesAsked = setOf(
            AllResources.GroupsPositions.name,
            AllResources.GroupsCount.name,
            AllResources.SelectedGroupIndex.name,
            AllResources.GroupZoneStartRadios.name,
            AllResources.GroupZoneEndRadios.name,
            AllResources.LabelYLimits.name,
            AllResources.SelectedIconIndex.name,
        ),
        textures = emptyMap(),
        code =
        """
#version 100
#ifdef GL_ES
precision mediump float;
#endif
#define MAX_ICONS 100
uniform vec2 GroupsPositions[MAX_ICONS];
uniform vec2 LabelYLimits;
uniform int GroupsCount;
uniform int SelectedGroupIndex;
uniform int SelectedIconIndex;
uniform float GroupZoneStartRadios;
uniform float GroupZoneEndRadios;

#define adjust vec2(0.0, -40.0)
#define sr 100.0

void main() {
    float selectionDist = distance(gl_FragCoord.xy, GroupsPositions[SelectedGroupIndex] + adjust);
    int isInGroupZone = 0;
    int isInLabelZone = 0;
    if(selectionDist > GroupZoneStartRadios-100.0 && selectionDist < GroupZoneEndRadios+100.0 && gl_FragCoord.y < GroupsPositions[0].y + 100.0  && gl_FragCoord.y > GroupsPositions[GroupsCount-1].y - 100.0){
        isInGroupZone = 1; }
    if(gl_FragCoord.y < LabelYLimits.x && gl_FragCoord.y > LabelYLimits.y){
        isInLabelZone = 1; }

    if(SelectedGroupIndex == -1){
        // todo something repetitive
        gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0);
        return;
    }

    float mdist = 10000000.;
    int ci;
    for(int i=0; i<GroupsCount; ++i){
        float d = distance(gl_FragCoord.xy, GroupsPositions[i] + adjust);
        if (d<mdist){
            mdist = d;
            ci = i;
        }
    }

    if(ci == SelectedGroupIndex){
        if(mdist < sr){
            gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);
            return;
        }
    }
    if(ci < SelectedGroupIndex && mdist < sr){
        gl_FragColor = vec4(0.0, 0.0, 1.0, 1.0);
        return;
    }
    if(ci > SelectedGroupIndex && mdist < sr){
        gl_FragColor = vec4(0.0, 1.0, 1.0, 1.0);
        return;
    }
    if(isInGroupZone == 1){
        if(isInLabelZone == 1 && SelectedIconIndex != -1){
            gl_FragColor = vec4(0.8, 0.8, 0.8, 1.0);
            return;
        }
        gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);
    }
    else{
        if(isInLabelZone == 1 && SelectedIconIndex != -1){
            gl_FragColor = vec4(0.8, 0.8, 0.8, 1.0);
            return;
        }
        gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0);
    }
}
""",
    ),

    ShaderData(
        name = "fluid volume",
        resourcesAsked = setOf(
            AllResources.Volume.name,
            AllResources.DarkMode.name,
            AllResources.Gyroscope.name
        ),
        textures = emptyMap(),
        code =
"""
#version 100

#ifdef GL_ES
precision mediump float;
#endif

#define MAX_ICONS 100

uniform vec2 u_resolution;
uniform float Volume;
uniform int DarkMode;
uniform vec3 Gyroscope;

void main() {
    vec4 color = insideCircle * vec4(0.5, 0.5, 0.5, 1.0);

    gl_FragColor = color;
}
"""
    )
)