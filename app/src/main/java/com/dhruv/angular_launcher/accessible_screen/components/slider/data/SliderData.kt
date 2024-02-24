package com.dhruv.angular_launcher.accessible_screen.components.slider.data

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import com.dhruv.angular_launcher.data.models.SelectionMode

@Stable
data class SliderData(
    val visible: Boolean = false,
    val shouldUpdateOffset: Boolean = false,
    val shouldUpdateSelection: Boolean = false,
    val touchPos: Offset = Offset.Zero,
    val selectionMode: SelectionMode = SelectionMode.NotSelected
)