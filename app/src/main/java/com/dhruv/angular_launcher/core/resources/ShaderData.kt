package com.dhruv.angular_launcher.core.resources

data class ShaderData(
    val name: String = "unnamed",
    val resourcesAsked: Set<String> = emptySet(),
    val textures: Map<String, Int> = emptyMap(),
    val code: String =
        """
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

    gl_FragColor = vec4(color, 1.0);
}
"""
) {
    companion object {
        private const val DELIMITER = "|"
        private const val EMPTY = "EMPTY"

        fun fromString(str: String): ShaderData {
            val parts = str.split(DELIMITER)
            require(parts.size == 4) { "Invalid string format for ShaderData : $str" }

            val name = parts[0]
            val resourcesStr = parts[0]
            val resourcesAsked = if (resourcesStr == EMPTY) emptySet() else
                resourcesStr.split(",").toSet()
            val texturesStr = parts[2]
            val textures = if (texturesStr == EMPTY) emptyMap() else {
                texturesStr.split(",").map { texture ->
                    val (key, value) = texture.split("=")
                    key to value.toInt()
                }.toMap()
            }

            val code = parts[3]
            return ShaderData(name, resourcesAsked, textures, code)
        }
    }

    override fun toString(): String {
        val resourcesStr = if (resourcesAsked.isEmpty()) EMPTY else
            resourcesAsked.joinToString(",") { it }
        val texturesStr = if (textures.isEmpty()) {
            EMPTY
        } else {
            textures.entries.joinToString(",") { "${it.key}=${it.value}" }
        }
        return "$name$DELIMITER$resourcesStr$DELIMITER$texturesStr$DELIMITER$code"
    }
}