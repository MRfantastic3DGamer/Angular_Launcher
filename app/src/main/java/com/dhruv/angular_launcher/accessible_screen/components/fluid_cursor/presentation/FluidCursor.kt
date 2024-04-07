package com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.FluidCursorVM

@Composable
fun FluidCursor (vm: FluidCursorVM){

    val targetPos = vm.selectedIconOffset?: if (vm.isSliderOnFocus) vm.selectedGroupOffset?: vm.touchPos else vm.touchPos

//    vm.updateShaderCursorPos(targetPos.x, targetPos.y)

//    DebugLayerValues.addString("touchPos", vm.touchPos.toString())
//    DebugLayerValues.addString("selectedGroupOffset", vm.selectedGroupOffset.toString())
//    DebugLayerValues.addString("selectedIconOffset", vm.selectedIconOffset.toString())

//    DebugLayerValues.addString("targetPos", targetPos.toString())

//    println("touch pos${vm.touchPos}, selectedGroup pos${vm.selectedGroupOffset}, selectedIcon pos${vm.selectedIconOffset}")

    Box(modifier = Modifier
        .size(10.dp)
        .offset { targetPos.round() }
    )

//    Box(modifier = Modifier
//        .fillMaxSize()
//        .background(
//            Brush.radialGradient(
//                colorStops = arrayOf(
//                    0.00f to vm.color.withAlpha(0f),
//                    mag - 0.04f to vm.color.withAlpha(0f),
//                    mag - 0.03f to vm.color.withAlpha(0.5f),
//                    mag - 0.02f to vm.color.withAlpha(0.8f),
//                    mag - 0.01f to vm.color.withAlpha(0.5f),
//                    mag - 0.005f to vm.color.withAlpha(1f),
//                    mag - 0f to vm.color.withAlpha(0f),
//                ),
//                center = vm.targetPos
//            )
//        )
//    )
}

fun Color.withAlpha (A: Float): Color {
    return this.copy(alpha = A)
}

@Preview
@Composable
private fun CursorPrev() {
    FluidCursor(vm = FluidCursorVM())
}