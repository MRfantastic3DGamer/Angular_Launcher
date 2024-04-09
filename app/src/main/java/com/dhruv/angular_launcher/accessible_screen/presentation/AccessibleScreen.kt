package com.dhruv.angular_launcher.accessible_screen.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.dhruv.angular_launcher.accessible_screen.AccessibleScreenVM
import com.dhruv.angular_launcher.accessible_screen.components.app_label.AppLabelVM
import com.dhruv.angular_launcher.accessible_screen.components.app_label.presentation.AppLabel
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.FluidCursorVM
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.presentation.FluidCursor
import com.dhruv.angular_launcher.accessible_screen.components.glsl_art.MyGLSurfaceContent
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigatorVM
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.presentation.RadialAppNavigation
import com.dhruv.angular_launcher.accessible_screen.components.slider.SliderVM
import com.dhruv.angular_launcher.accessible_screen.components.slider.presentation.Slider
import com.dhruv.angular_launcher.core.database.room.ThemeDatabase
import com.dhruv.angular_launcher.core.database.room.models.getIconPositioningSchemes
import com.dhruv.angular_launcher.core.database.room.models.getRenderer
import com.dhruv.angular_launcher.interaction_calculation.AccessibleScreenTrigger
import com.dhruv.angular_launcher.settings_screen.SettingsVM
import com.dhruv.angular_launcher.settings_screen.presentation.SettingsScreen

@Composable
fun AccessibleScreen(mainScreenVM: AccessibleScreenVM, settingsVM: SettingsVM) {

    val context = LocalContext.current
    val contentResolver = context.contentResolver
//    var renderer by remember { mutableStateOf(MyGLRenderer(resources, PrefValues.t_shader)) }

    val themeVM by remember { mutableStateOf(ThemeDatabase.getViewModel(context)) }

    val sliderVM by remember { mutableStateOf(SliderVM(openSettings = settingsVM::openSettings)) }
    val appNavigatorVM by remember { mutableStateOf(RadialAppNavigatorVM()) }
    val appLabelVM by remember { mutableStateOf(AppLabelVM()) }
    val fluidCursorVM by remember { mutableStateOf(FluidCursorVM()) }

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
            themeVM.renderer?.let { MyGLSurfaceContent(it) }
        }

        when (settingsVM.settingsOpened) {
            true -> {
                SettingsScreen(vm = settingsVM, exitSettings = {
                    settingsVM.exitSettings(it, themeVM.currTheme.getIconPositioningSchemes())
                    themeVM.renderer = themeVM.currTheme.getRenderer(contentResolver)
                })
            }

            false -> {
                RadialAppNavigation(vm = appNavigatorVM)

                FluidCursor(vm = fluidCursorVM)
                
                Slider(vm = sliderVM)

                AppLabel(vm = appLabelVM)
            }
        }
    }
}