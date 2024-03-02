package com.dhruv.angular_launcher.accessible_screen.presentation

import android.app.WallpaperManager
import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import com.dhruv.angular_launcher.accessible_screen.AccessibleScreenVM
import com.dhruv.angular_launcher.accessible_screen.components.app_label.AppLabelVM
import com.dhruv.angular_launcher.accessible_screen.components.app_label.presentation.AppLabel
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.FluidCursorVM
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.presentation.FluidCursor
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigatorVM
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.presentation.RadialAppNavigation
import com.dhruv.angular_launcher.accessible_screen.components.slider.SliderVM
import com.dhruv.angular_launcher.accessible_screen.components.slider.presentation.Slider
import com.dhruv.angular_launcher.interaction_calculation.AccessibleScreenTrigger
import com.dhruv.angular_launcher.settings_screen.SettingsVM
import com.dhruv.angular_launcher.settings_screen.presentation.SettingsScreen
import com.dhruv.angular_launcher.utils.ScreenUtils

@Composable
fun AccessibleScreen(mainScreenVM: AccessibleScreenVM, settingsVM: SettingsVM){

    val context = LocalContext.current

    // Retrieve the device's wallpaper
    val wallpaperDrawable = remember { WallpaperManager.getInstance(context).drawable }


    val sliderVM by remember { mutableStateOf(SliderVM()) }
    val appNavigatorVM by remember { mutableStateOf(RadialAppNavigatorVM()) }
    val appLabelVM by remember { mutableStateOf(AppLabelVM()) }
    val fluidCursorVM by remember { mutableStateOf(FluidCursorVM()) }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .wallpaper(wallpaperDrawable)
            .AccessibleScreenTrigger(context, {settingsVM.openSettings(context)}, !settingsVM.settingsOpened)
    ) {
        when (settingsVM.settingsOpened) {
            true -> {
                SettingsScreen(vm = settingsVM, exitSettings = settingsVM::exitSettings)
            }
            false -> {
                Slider(vm = sliderVM)

                FluidCursor(vm = fluidCursorVM)

                RadialAppNavigation(vm = appNavigatorVM)

                AppLabel(vm = appLabelVM)
            }
        }
    }
//    Trigger(openSettings = settingsVM::openSettings)
}

fun Modifier.wallpaper( drawable: Drawable? ): Modifier {
    return if (drawable != null)
        this
            .background(
                ShaderBrush(ImageShader(drawable.toBitmap(
                    width = ScreenUtils.screenWidth.toInt(),
                    height = ScreenUtils.screenHeight.toInt()
                ).asImageBitmap()))
            )
    else this
}