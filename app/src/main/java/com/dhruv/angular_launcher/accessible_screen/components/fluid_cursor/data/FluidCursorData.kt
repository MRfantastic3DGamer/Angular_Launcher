package com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data

import androidx.compose.ui.geometry.Offset

data class FluidCursorData(
    val visibility: Boolean = false,
    val selectedIconOffset: Offset? = null,
    val selectedGroupOffset: Offset? = null,
    val isSliderOnFocus: Boolean = true,
    val touchPos: Offset = Offset.Zero,
)