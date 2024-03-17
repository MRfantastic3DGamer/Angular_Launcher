package com.dhruv.angular_launcher.accessible_screen.components.glsl_art

import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.dhruv.shader_test.opengl_renderer.ui.MyGLSurfaceView

@Composable
fun MyGLSurfaceContent(renderer: MyGLRenderer) {
    AndroidView(
        factory = { context ->
            MyGLSurfaceView(context, renderer).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}