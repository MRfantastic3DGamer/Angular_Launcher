package com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class FluidCursorPersistentData(
    val fluidCursorLooks: FluidCursorLooks,
    val animationSpeed: Float,
)