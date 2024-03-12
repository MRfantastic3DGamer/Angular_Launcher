package com.dhruv.angular_launcher.accessible_screen.components.app_label

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.dhruv.angular_launcher.accessible_screen.components.app_label.data.AppLabelValue

class AppLabelVM: ViewModel() {
    var offset: Offset by mutableStateOf(Offset.Zero)
    var appPkg: String by mutableStateOf("")
    var prevPkg: String by mutableStateOf("")
    var visibility: Boolean by mutableStateOf(false)
    val height: Float = 300f

    var iconJiggleTrigger by mutableStateOf(false)
    init {
        AppLabelValue.GetData.observeForever {
            offset = it.position
            if (it.appPackage != appPkg){
                prevPkg = appPkg
                appPkg = it.appPackage
                iconJiggleTrigger = true
            }
            visibility = appPkg != "@"
        }
    }
}