package com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data

import androidx.compose.ui.geometry.Offset

data class FluidCursorData(
    val targetPosition: Offset = Offset.Zero,
    val urgency: Float = 1f,
    val snap: Boolean = false,
    val visibility: Boolean = false,
    val state: CursorState = CursorState.STUCK_TO_SLIDER
)
