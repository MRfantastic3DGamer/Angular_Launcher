package com.dhruv.angular_launcher.accessible_screen.components.glsl_art

import com.dhruv.angular_launcher.core.database.prefferences.ShaderData

val DefaultShaders = listOf(
    ShaderData(
        name = "blob apps",
        textures = emptyMap(),
        code =
"""
void main() {
    
    float value = 1./distance(gl_FragCoord.xy, u_mouse) * 1.2;
    vec2 t_pos = vec2(0., 0.);
    float t_dist = 100000.;

    for(int i=0;i<MAX_ICONS;++i)
    {
        if (u_positions_X[i] == -10000.)
            break;
        t_pos = vec2(u_positions_X[i], u_positions_Y[i]);
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
        name = "planetary apps",
        textures = mapOf(),
        code =
"""
    
"""
    )
)