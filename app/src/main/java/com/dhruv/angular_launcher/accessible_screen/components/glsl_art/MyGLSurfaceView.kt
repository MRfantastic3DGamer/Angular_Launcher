package com.dhruv.shader_test.opengl_renderer.ui

import android.content.Context
import android.opengl.GLSurfaceView
import com.dhruv.angular_launcher.accessible_screen.components.glsl_art.MyGLRenderer

class MyGLSurfaceView(context: Context, renderer: MyGLRenderer) : GLSurfaceView(context) {
    init {
        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2)

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)
    }
}