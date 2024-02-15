package com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset

@Stable
data class RadialAppNavigatorData(
    val center: Offset = Offset.Zero,
    val currentSelectionIndex: Int = 0,
)