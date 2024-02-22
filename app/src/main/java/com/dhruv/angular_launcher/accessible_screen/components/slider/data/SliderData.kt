package com.dhruv.angular_launcher.accessible_screen.components.slider.data

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset

@Stable
data class SliderData(
    val visible: Boolean = false,
    val shouldUpdateOffset: Boolean = false,
    val shouldUpdateSelection: Boolean = false,
    val touchPos: Offset = Offset.Zero,
    val elementsCount: Int = 0
)