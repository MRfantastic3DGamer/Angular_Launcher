package com.dhruv.angular_launcher.accessible_screen.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.dhruv.angular_launcher.accessible_screen.AccessibleScreenVM
import com.dhruv.angular_launcher.accessible_screen.components.app_label.AppLabelVM
import com.dhruv.angular_launcher.accessible_screen.components.app_label.presentation.AppLabel
import com.dhruv.angular_launcher.accessible_screen.components.glsl_art.MyGLRenderer
import com.dhruv.angular_launcher.accessible_screen.components.glsl_art.MyGLSurfaceContent
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
    val resources = context.resources
    val screenSize = android.util.Size(LocalConfiguration.current.screenWidthDp, LocalConfiguration.current.screenHeightDp)
    var renderer by remember { mutableStateOf(MyGLRenderer(screenSize, resources, PrefValues.t_shader)) }

    val sliderVM by remember { mutableStateOf(SliderVM(openSettings = settingsVM::openSettings)) }
    var appNavigatorVM by remember { mutableStateOf(RadialAppNavigatorVM(
        mousePosToShader = renderer::updateMousePos,
        iconPositionsToShader = renderer::setIcons
    )) }
    val appLabelVM by remember { mutableStateOf(AppLabelVM()) }

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

        if (!settingsVM.settingsOpened) {
            MyGLSurfaceContent(renderer)
        }

        when (settingsVM.settingsOpened) {
            true -> {
                SettingsScreen(vm = settingsVM, exitSettings = {
                    settingsVM.exitSettings(it)
                    renderer = MyGLRenderer(screenSize, resources, PrefValues.t_shader)
                    appNavigatorVM = appNavigatorVM.apply {
                        mousePosToShader = renderer::updateMousePos
                        iconPositionsToShader = renderer::setIcons
                    }
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