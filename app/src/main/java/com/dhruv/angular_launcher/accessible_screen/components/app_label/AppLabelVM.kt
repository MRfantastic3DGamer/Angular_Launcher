package com.dhruv.angular_launcher.accessible_screen.components.app_label

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.dhruv.angular_launcher.accessible_screen.components.app_label.data.AppLabelValue
import com.dhruv.angular_launcher.database.apps_data.AppsIconsDataValues
import com.example.launcher.Drawing.DrawablePainter

class AppLabelVM: ViewModel() {
    var offset: Offset by mutableStateOf(Offset.Zero)
    var appPkg: String by mutableStateOf("")
    var visibility: Boolean by mutableStateOf(false)
    val height: Float = 300f

    var appsIcons: Map<String, DrawablePainter> by mutableStateOf(emptyMap())

    init {
        AppsIconsDataValues.getAppsIcons.observeForever { if (it != null) appsIcons = it }
        AppLabelValue.GetData.observeForever {
            offset = it.position
            appPkg = it.appPackage
            visibility = it.appPackage != "@"
        }
    }
}