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
        resourcesAsked = setOf(),
        code =
"""
#version 100
#ifdef GL_FRAGMENT_PRECISION_HIGH
precision highp float;
#else
precision mediump float;
#endif

varying vec2 vTexCoord;
uniform sampler2D TEX0;

void main() {
    vec4 color = texture2D(TEX0, vTexCoord);
    gl_FragColor = color;
}
""",
    ),

    ShaderData(
        name = "track cursor",
        resourcesAsked = setOf(
            AllResources.TouchPosition.name,
            AllResources.TouchStartPositions.name,
        ),
        code =
        """
#version 100
#ifdef GL_FRAGMENT_PRECISION_HIGH
precision highp float;
#else
precision mediump float;
#endif

#define MAX_ICONS 100

uniform vec2 TouchPosition;
uniform vec2 TouchStartPositions;

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
    vec4 blackColor = vec4(0.0, 0.0, 0.0, 1.0);  // Black

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
            AllResources.GroupsPositioning.name,
            AllResources.SelectedGroupIndex.name,
            AllResources.GroupsCount.name,
        ),
        code =
"""
#version 100
#ifdef GL_FRAGMENT_PRECISION_HIGH
precision highp float;
#else
precision mediump float;
#endif

#define MAX_ICONS 100

uniform vec2 TouchPosition;
uniform vec2 IconsPositions[MAX_ICONS];
uniform int IconsCount;

uniform vec4 GroupsPositioning;
uniform int SelectedGroupIndex;
uniform int GroupsCount;

#define adjust vec2(0.0, -40.0)

void main() {

    float value = 1./distance(gl_FragCoord.xy, TouchPosition) * 1.2;
    vec2 t_pos = vec2(0., 0.);
    float t_dist = 100000.;

    for(int i=0;i<IconsCount;++i){
        t_pos = IconsPositions[i] + adjust;
        t_dist = min(t_dist, distance(gl_FragCoord.xy, t_pos));
    }
    for(int i=0;i<GroupsCount;++i){
        t_pos = vec2(
            GroupsPositioning.x - GroupsPositioning.w * (1.0 - clamp(abs(float(i) - float(SelectedGroupIndex)), 0.0, 1.0)),
            GroupsPositioning.y - float(i) * GroupsPositioning.z
        ) + adjust;
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
            AllResources.GroupsPositioning.name,
            AllResources.GroupsCount.name,
            AllResources.SelectedGroupIndex.name,
            AllResources.GroupZoneStartRadios.name,
            AllResources.GroupZoneEndRadios.name,
            AllResources.LabelYLimits.name,
            AllResources.SelectedIconIndex.name,
            AllResources.TouchPosition.name,
        ),
        code =
        """
#version 100
#ifdef GL_FRAGMENT_PRECISION_HIGH
precision highp float;
#else
precision mediump float;
#endif

uniform vec4 GroupsPositioning;
uniform int GroupsCount;
uniform int SelectedGroupIndex;
uniform float GroupZoneStartRadios;
uniform float GroupZoneEndRadios;
uniform vec2 LabelYLimits;
uniform vec2 TouchPosition;

#define adjust vec2(0.0, -40.0)
#define sr 100.0

void main() {
    float groupsX = GroupsPositioning.x;
    float groupsYMax = GroupsPositioning.y;
    float groupsYMin = GroupsPositioning.y - GroupsPositioning.z * float(GroupsCount);
    vec2 selectedGroupPos = vec2(groupsX - GroupsPositioning.w, GroupsPositioning.y - GroupsPositioning.z * float(SelectedGroupIndex));
    
    float selectionDist = distance(gl_FragCoord.xy, selectedGroupPos + adjust);
    float touchDist = distance(gl_FragCoord.xy, TouchPosition);
    int isInGroupZone = 0;
    int isInGroupsOptionsZone = 0;
    int isInLabelZone = 0;
    int isNearTouch = 0;
    if(gl_FragCoord.y < LabelYLimits.x && gl_FragCoord.y > LabelYLimits.y){
        isInLabelZone = 1; }
    if(selectionDist > GroupZoneStartRadios && selectionDist < GroupZoneEndRadios && gl_FragCoord.y < groupsYMax  && gl_FragCoord.y > groupsYMin){
        isInGroupZone = 1; }
    if(gl_FragCoord.x > groupsX && gl_FragCoord.y < groupsYMax && gl_FragCoord.y > groupsYMin){
        isInGroupsOptionsZone = 1; }
    if(touchDist < 50.0){
        isNearTouch = 1; }

    if(SelectedGroupIndex == -1){
        // todo something repetitive
        gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0);
        return;
    }

    

    if(isNearTouch == 1){
        gl_FragColor = vec4(1.0);
        return;
    }
    if(isInGroupsOptionsZone == 1){
        gl_FragColor = vec4(0.5);
        return;
    }
    if(isInLabelZone == 1){
        gl_FragColor = vec4(0.7);
        return;
    }
    if(isInGroupZone == 1){
        gl_FragColor = vec4(0.3);
        return;
    }
    gl_FragColor = vec4(0.0);
}
""",
    ),

    ShaderData(
        name = "LGBT polka",
        resourcesAsked = setOf(
            AllResources.IconsCount.name,
            AllResources.IconsPositions.name,
            AllResources.SelectedIconIndex.name,
            AllResources.GroupsPositioning.name,
            AllResources.GroupsCount.name,
            AllResources.SelectedGroupIndex.name,
            AllResources.GroupZoneStartRadios.name,
            AllResources.GroupZoneEndRadios.name,
            AllResources.TouchPosition.name,
            AllResources.Frame.name,
        ),
        code =
        """
#version 100
#ifdef GL_FRAGMENT_PRECISION_HIGH
precision highp float;
#else
precision mediump float;
#endif

uniform vec4 GroupsPositioning;
uniform float GroupZoneStartRadios;
uniform float GroupZoneEndRadios;
uniform int GroupsCount;
uniform int SelectedGroupIndex;

uniform int IconsCount;
uniform int SelectedIconIndex;
uniform vec2 IconsPositions[100];

uniform vec2 TouchPosition;

uniform int Frame;

#define sr 100.0
#define adjust vec2(0.0, -40.0)

#define spacing 10.0
#define dotSize 5.0
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

    vec2 center = vec2(GroupsPositioning.x - GroupsPositioning.w, GroupsPositioning.y - GroupsPositioning.z * float(SelectedGroupIndex)) + adjust;
    float selectionDist = distance(gl_FragCoord.xy, center);
    
    float wave = smoothstep(0.0, 1.0, sin(float(Frame) * 0.1 - distance(gl_FragCoord.xy ,TouchPosition) * 0.01)*1.2);
    float sliderH = smoothstep(-100.0, 0.0, GroupsPositioning.x - 130.0 - gl_FragCoord.x);
    float sliderV = smoothstep(-100.0, 0.0, GroupsPositioning.y + 50.0 - gl_FragCoord.y) * smoothstep(-100.0, 0.0, gl_FragCoord.y - GroupsPositioning.y - GroupsPositioning.z * float(GroupsCount) + 50.0);
    float slider = (1.0 - sliderH) * sliderV;

    float t_dist = 100000.;
    for(int i=0;i<IconsCount;++i)
    {
        t_dist = min(t_dist, distance(gl_FragCoord.xy, IconsPositions[i]+adjust));
    }
    float iconsBoundary = smoothstep(0.1, 1.0, smoothstep(1./50.0, 1./t_dist, 1./60.0));

    float mask = clamp(0.0, -wave*0.2 + slider + iconsBoundary, 1.0);
    if(SelectedGroupIndex == -1){ mask = 0.0; }

    gl_FragColor = vec4(triPolka(mask), 1.0);
}
""",
    ),

    ShaderData(
        name = "fluid volume",
        resourcesAsked = setOf(
            AllResources.Resolution.name,
            AllResources.RotationAngles.name,
            AllResources.Volume.name,
        ),
        code =
        """
#version 100
#ifdef GL_FRAGMENT_PRECISION_HIGH
precision highp float;
#else
precision mediump float;
#endif

uniform vec2 Resolution;
uniform vec3 RotationAngles;
uniform float Volume;

#define tilt RotationAngles.y

void main() {
    // Calculate the distance of each fragment from the line
    float y = gl_FragCoord.y - (gl_FragCoord.x * tan(tilt));
    float distanceFromLine = abs(y);

    // Define the width of the line
    float lineWidth = 5.0;
    float height = Resolution.y * Volume;
    float center = Resolution.x * 0.5;

    // Set the color based on the distance from the line
    float alpha = smoothstep(0.0, lineWidth, distanceFromLine);
    vec3 color = mix(vec3(0.0), vec3(1.0), step((gl_FragCoord.x - center) * tan(tilt) * 2.0 + height, gl_FragCoord.y));

    // Output the color
    gl_FragColor = vec4(color, alpha);
}
"""
    ),

)