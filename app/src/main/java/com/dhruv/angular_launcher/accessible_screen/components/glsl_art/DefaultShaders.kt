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
uniform vec2[MAX_ICONS] IconsPositions;
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

val DefaultShaders = listOf(
    ShaderData(
        name = "voronoi apps",
        textures = mapOf(),
        code =
"""

vec3 randomColor(int value) {
    // color for cursor
    if (value == -1){return vec3(.0);}
    // Use a hash function to ensure the same input produces the same output color
    float r = fract(sin(float(value) * 12.9898));
    float g = fract(sin(float(value) * 78.233));
    float b = fract(sin(float(value) * 45.7269));

    return vec3(r, g, b);
}

void main() {
    float dist = distance(u_mouse, gl_FragCoord.xy);
    float t_dist = dist;
    int closest = -1;

    for (int i = 0; i < MAX_ICONS; ++i) {
        if (u_positions_X[i] == -10000.0)
            break;

        t_dist = distance(vec2(u_positions_X[i], u_positions_Y[i]), gl_FragCoord.xy);
        if (t_dist < dist) {
            dist = t_dist;
            closest = i;
        }
    }

    gl_FragColor = vec4(randomColor(closest), 1.0);
}
"""
    )
)