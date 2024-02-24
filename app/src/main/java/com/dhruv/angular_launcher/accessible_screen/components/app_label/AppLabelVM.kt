package com.dhruv.angular_launcher.accessible_screen.components.app_label

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.dhruv.angular_launcher.accessible_screen.components.app_label.data.AppLabelValue

class AppLabelVM: ViewModel() {
    var offset: Offset by mutableStateOf(Offset.Zero)
    var appName: String by mutableStateOf("")
    var visibility: Boolean by mutableStateOf(false)
    val height: Float = 300f

    init {
        AppLabelValue.GetData.observeForever {
            offset = it.position
            appName = it.appPackage
            visibility = it.appPackage != "@"
        }
    }
}