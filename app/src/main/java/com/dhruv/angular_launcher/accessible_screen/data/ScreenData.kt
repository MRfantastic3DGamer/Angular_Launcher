package com.dhruv.angular_launcher.accessible_screen.data

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset

@Stable
data class ScreenData(
    val touchPosition: Offset = Offset.Zero,
    val delta: Offset? = null,
)
