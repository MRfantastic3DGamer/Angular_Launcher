package com.dhruv.angular_launcher.accessible_screen.components.app_label.data

import androidx.compose.ui.geometry.Offset

data class AppLabelData(
    val position: Offset = Offset.Zero,
    val appPackage: String = "app pkg name",
)
