package com.dhruv.angular_launcher.accessible_screen.presentation

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import com.dhruv.angular_launcher.accessible_screen.AccessibleScreenVM
import com.dhruv.angular_launcher.accessible_screen.components.app_label.AppLabelVM
import com.dhruv.angular_launcher.accessible_screen.components.app_label.presentation.AppLabel
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigatorVM
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.presentation.RadialAppNavigation
import com.dhruv.angular_launcher.accessible_screen.components.slider.SliderVM
import com.dhruv.angular_launcher.accessible_screen.components.slider.presentation.Slider
import com.dhruv.angular_launcher.core.database.prefferences.values.PrefValues
import com.dhruv.angular_launcher.interaction_calculation.AccessibleScreenTrigger
import com.dhruv.angular_launcher.settings_screen.SettingsVM
import com.dhruv.angular_launcher.settings_screen.presentation.SettingsScreen
import com.dhruv.angular_launcher.utils.BitmapFromURI

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AccessibleScreen(mainScreenVM: AccessibleScreenVM, settingsVM: SettingsVM){

    val context = LocalContext.current

    val sliderVM by remember { mutableStateOf(SliderVM(openSettings = settingsVM::openSettings)) }
    val appNavigatorVM by remember { mutableStateOf(RadialAppNavigatorVM()) }
    val appLabelVM by remember { mutableStateOf(AppLabelVM()) }

    val wallpaper = BitmapFromURI(context.contentResolver, Uri.parse(PrefValues.t_bg_path))

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .AccessibleScreenTrigger(
                context,
                { settingsVM.openSettings(context) },
                !settingsVM.settingsOpened
            )
    ) {
        if (wallpaper!=null){
            Image(
                bitmap = wallpaper.asImageBitmap(),
                contentDescription = "wallpaper",
                Modifier.fillMaxSize()
            )
            when (settingsVM.settingsOpened) {
                true -> {
                    SettingsScreen(vm = settingsVM, exitSettings = settingsVM::exitSettings)
                }
                false -> {
                    RadialAppNavigation(vm = appNavigatorVM)

                    Slider(vm = sliderVM)

                    AppLabel(vm = appLabelVM)
                }
            }
        }
    }
}