package com.dhruv.angular_launcher.accessible_screen.presentation

import android.content.res.Resources
import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.dhruv.angular_launcher.R
import com.dhruv.angular_launcher.accessible_screen.AccessibleScreenVM
import com.dhruv.angular_launcher.accessible_screen.components.app_label.AppLabelVM
import com.dhruv.angular_launcher.accessible_screen.components.app_label.presentation.AppLabel
import com.dhruv.angular_launcher.accessible_screen.components.glsl_art.composable.GLShader
import com.dhruv.angular_launcher.accessible_screen.components.glsl_art.renderer.SHADER_SOURCE
import com.dhruv.angular_launcher.accessible_screen.components.glsl_art.renderer.SHADER_START
import com.dhruv.angular_launcher.accessible_screen.components.glsl_art.renderer.ShaderRenderer
import com.dhruv.angular_launcher.accessible_screen.components.glsl_art.renderer.readTextFileFromResource
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigatorVM
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.presentation.RadialAppNavigation
import com.dhruv.angular_launcher.accessible_screen.components.slider.SliderVM
import com.dhruv.angular_launcher.accessible_screen.components.slider.presentation.Slider
import com.dhruv.angular_launcher.core.database.prefferences.values.PrefValues
import com.dhruv.angular_launcher.interaction_calculation.AccessibleScreenTrigger
import com.dhruv.angular_launcher.settings_screen.SettingsVM
import com.dhruv.angular_launcher.settings_screen.presentation.SettingsScreen

@Composable
fun AccessibleScreen(mainScreenVM: AccessibleScreenVM, settingsVM: SettingsVM) {

    val context = LocalContext.current

//    var fragmentShader by remember { mutableStateOf(SHADER_START + "\n" + PrefValues.t_shader) }
    var fragmentShader = context.readTextFileFromResource(R.raw.planet_apps)
    val vertexShader = context.readTextFileFromResource(R.raw.simple_vertex_shader)

    val superNoise1 by remember { mutableStateOf(BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.super_noise_1)) }

    var shaderRenderer = remember {
        ShaderRenderer().apply {
            setShaders(
                fragmentShader,
                vertexShader,
                SHADER_SOURCE
            )
        }
    }
    LaunchedEffect(key1 = superNoise1) {
        if (superNoise1 != null) {
            shaderRenderer = shaderRenderer.apply {
                setTexture("superNoise1", superNoise1)
            }
        }
    }

    val sliderVM by remember { mutableStateOf(SliderVM(openSettings = settingsVM::openSettings)) }
    val appNavigatorVM by remember { mutableStateOf(RadialAppNavigatorVM(
        mousePosToShader = shaderRenderer::updateMousePos,
        iconPositionsToShader = shaderRenderer::setIcons
    )) }
    val appLabelVM by remember { mutableStateOf(AppLabelVM()) }

//    val wallpaper = BitmapFromURI(context.contentResolver, Uri.parse(PrefValues.t_bg_path))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .AccessibleScreenTrigger(
                context,
                { settingsVM.openSettings(context) },
                !settingsVM.settingsOpened
            )
    ) {
//        if (wallpaper!=null) {
//            Image(
//                bitmap = wallpaper.asImageBitmap(),
//                contentDescription = "wallpaper",
//                Modifier.fillMaxSize()
//            )
//         }

        GLShader(
            modifier = Modifier
                .fillMaxSize(),
            renderer = shaderRenderer,
        )

        when (settingsVM.settingsOpened) {
            true -> {
                SettingsScreen(vm = settingsVM, exitSettings = {
                    fragmentShader = SHADER_START + "\n" + PrefValues.t_shader
                    settingsVM.exitSettings(it)
//                    shaderRenderer = shaderRenderer.apply {
//                        shaderRenderer.setShaders(
//                            fragmentShader,
//                            vertexShader,
//                            SHADER_SOURCE
//                        )
//                    }
//                    println(fragmentShader)
                })
            }

            false -> {
                RadialAppNavigation(vm = appNavigatorVM)

                Slider(vm = sliderVM)

                AppLabel(vm = appLabelVM)
            }
        }
    }
}