package com.dhruv.angular_launcher.accessible_screen.components.glsl_art.composable

import android.opengl.GLSurfaceView.DEBUG_CHECK_GL_ERROR
import android.opengl.GLSurfaceView.DEBUG_LOG_GL_CALLS
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.dhruv.angular_launcher.R
import com.dhruv.angular_launcher.accessible_screen.components.glsl_art.renderer.SHADER_SOURCE
import com.dhruv.angular_launcher.accessible_screen.components.glsl_art.renderer.ShaderRenderer
import com.dhruv.angular_launcher.accessible_screen.components.glsl_art.renderer.readTextFileFromResource
import com.dhruv.angular_launcher.accessible_screen.components.glsl_art.view.ShaderGLSurfaceView

@Composable
fun GLShader(
    renderer: ShaderRenderer,
    modifier: Modifier = Modifier
) {

    var view: ShaderGLSurfaceView? = remember {
        null
    }

    val lifeCycleState = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(key1 = lifeCycleState) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    view?.onResume()
                    renderer.onResume()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    view?.onPause()
                    renderer.onPause()
                }
                else -> {
                }
            }
        }
        lifeCycleState.addObserver(observer)

        onDispose {
//            Timber.d("View Disposed ${view.hashCode()}")
            lifeCycleState.removeObserver(observer)
            view?.onPause()
            view = null
        }
    }

    AndroidView(modifier = modifier,
        factory = {
            ShaderGLSurfaceView(it)
        }) { glSurfaceView ->
        view = glSurfaceView
        glSurfaceView.debugFlags = DEBUG_CHECK_GL_ERROR or DEBUG_LOG_GL_CALLS
        glSurfaceView.setShaderRenderer(renderer)
    }
}

@Preview
@Composable
private fun ShaderPreview() {
    val context = LocalContext.current
    var fragmentShader = context.readTextFileFromResource(R.raw.fluid_points_shader)
    val vertexShader = context.readTextFileFromResource(R.raw.simple_vertex_shader)


    var renderer = remember {
        ShaderRenderer().apply {
            setShaders(
                fragmentShader,
                vertexShader,
                SHADER_SOURCE
            )
        }
    }
    GLShader(
        modifier = Modifier.fillMaxSize(),
        renderer = renderer
    )
}