package com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.FluidCursorVM
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.CursorState

@Composable
fun FluidCursor (vm: FluidCursorVM){
    val targetMag = if (vm.visibility) when (vm.cursorState) {
        CursorState.FREE -> vm.freeRadius
        CursorState.STUCK_TO_SLIDER -> vm.sliderStuckRadius
        CursorState.STUCK_TO_ICON -> vm.appStuckRadius
    } else 10f

    val mag by animateFloatAsState(targetValue = targetMag, label = "cursor-size")

    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            Brush.radialGradient(
                colorStops = arrayOf(
                    0.00f to vm.color.withAlpha(0f),
                    mag - 0.04f to vm.color.withAlpha(0f),
                    mag - 0.03f to vm.color.withAlpha(0.5f),
                    mag - 0.02f to vm.color.withAlpha(0.8f),
                    mag - 0.01f to vm.color.withAlpha(0.5f),
                    mag - 0.005f to vm.color.withAlpha(1f),
                    mag - 0f to vm.color.withAlpha(0f),
                ),
                center = vm.targetPos
            )
        )
    )
}

fun Color.withAlpha (A: Float): Color {
    return this.copy(alpha = A)
}

@Preview
@Composable
private fun CursorPrev() {
    FluidCursor(vm = FluidCursorVM())
}