package com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorLooks
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorValues

class FluidCursorVM: ViewModel() {
    var targetPos: Offset by mutableStateOf(Offset.Zero)
    var points: List<Offset> by mutableStateOf((0..40).map { Offset.Zero })
    var fluidCursorLooks: FluidCursorLooks by mutableStateOf(FluidCursorLooks())
    var animationSpeed: Float by mutableStateOf(1f)

    init {
        FluidCursorValues.GetPersistentData.observeForever {
            fluidCursorLooks = it.fluidCursorLooks
            animationSpeed = it.animationSpeed
        }
        FluidCursorValues.GetData.observeForever {
            targetPos = it.targetPosition
            // TODO: points = simulatePoints()
        }
    }
}