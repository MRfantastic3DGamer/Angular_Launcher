package com.dhruv.angular_launcher.core.resources

import android.content.ContentResolver
import android.graphics.ImageDecoder
import android.net.Uri

data class ShaderData(
    val name: String = "blob apps",
    val resourcesAsked: Set<String> = setOf(
        AllResources.TouchPosition.name,
        AllResources.IconsPositions.name,
        AllResources.IconsCount.name,
    ),
    val textures: List<String> = emptyList(),
    val code: String =
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
) {
    companion object {
        private const val DELIMITER = "|#|"
        private const val EMPTY = "EMPTY"
        private const val SEP = "#!#"


        fun fromString(str: String): ShaderData {
            val parts = str.split(DELIMITER)
            require(parts.size == 4) { "Invalid string format for ShaderData : $str" }

            val name = parts[0]
            val resourcesStr = parts[1]
            val resourcesAsked = if (resourcesStr == EMPTY) emptySet() else
                resourcesStr.split(SEP).toSet()
            val texturesStr = parts[2]
            val textures = if (texturesStr == EMPTY) emptyList() else
                texturesStr.split(SEP).toList()

            val code = parts[3]
            return ShaderData(name, resourcesAsked, textures, code)
        }
    }

    override fun toString(): String {
        val resourcesStr = if (resourcesAsked.isEmpty()) EMPTY else resourcesAsked.joinToString(SEP)
        val texturesStr = if (textures.isEmpty()) EMPTY else textures.joinToString(SEP)
        return "$name$DELIMITER$resourcesStr$DELIMITER$texturesStr$DELIMITER$code"
    }
}

fun ShaderData.textureBitmaps(contentResolver: ContentResolver) = textures.mapIndexed { i, uri ->
    val source = ImageDecoder.createSource(contentResolver, Uri.parse(uri))
    val bitmap = ImageDecoder.decodeBitmap(source)
    "TEX$i" to bitmap
}