package com.dhruv.angular_launcher.accessible_screen.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.dhruv.angular_launcher.accessible_screen.AccessibleScreenVM
import com.dhruv.angular_launcher.accessible_screen.components.app_label.AppLabelVM
import com.dhruv.angular_launcher.accessible_screen.components.app_label.presentation.AppLabel
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigatorVM
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.presentation.RadialAppNavigation
import com.dhruv.angular_launcher.accessible_screen.components.slider.SliderVM
import com.dhruv.angular_launcher.accessible_screen.components.slider.presentation.Slider

@Composable
fun AccessibleScreen(vm: AccessibleScreenVM){

    val sliderVM by remember { mutableStateOf(SliderVM()) }
    val appNavigatorVM by remember { mutableStateOf(RadialAppNavigatorVM()) }
    val appLabelVM by remember { mutableStateOf(AppLabelVM()) }

    Box (
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Text(text = vm.navigationMode .name)
            Text(text = vm.selectionMode  .name)
            Text(text = vm.navigationStage.name)
        }

        Slider(vm = sliderVM)

        RadialAppNavigation(vm = appNavigatorVM)

        AppLabel(vm = appLabelVM)
    }
}