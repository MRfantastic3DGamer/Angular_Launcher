package com.dhruv.angular_launcher.accessible_screen.components.blender

import org.intellij.lang.annotations.Language

@Language("AGSL")
const val BlendingShader = """
    uniform shader composable;
    
    uniform float visibility;
    
    half4 main(float2 cord) {
        half4 color = composable.eval(cord);
        color.a = step(visibility, color.a);
        return color;
    }
"""