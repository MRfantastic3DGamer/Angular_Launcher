package com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.CursorState
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorValues

class FluidCursorVM: ViewModel() {
    var targetPos: Offset by mutableStateOf(Offset.Zero)
    var animationSpeed: Float by mutableStateOf(1f)
    var snap: Boolean by mutableStateOf(false)
    var visibility:Boolean by mutableStateOf(false)
    var color: Color by mutableStateOf(Color.White)
    var cursorState: CursorState by mutableStateOf(CursorState.STUCK_TO_SLIDER)
    var freeRadius: Float by mutableStateOf(0.07f)
    var appStuckRadius: Float by mutableStateOf(0.15f)
    var sliderStuckRadius: Float by mutableStateOf(0.155f)

    init {
        FluidCursorValues.GetPersistentData.observeForever {
            animationSpeed = it.animationSpeed
            color = it.fluidCursorLooks.color
        }
        FluidCursorValues.GetData.observeForever {
            targetPos = it.targetPosition
            snap = it.snap
            visibility = it.visibility
            cursorState = it.state
            // TODO: points = simulatePoints()
        }
    }
}