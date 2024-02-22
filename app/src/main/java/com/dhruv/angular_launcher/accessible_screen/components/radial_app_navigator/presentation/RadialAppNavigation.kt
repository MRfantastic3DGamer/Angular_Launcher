package com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorData
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorValues
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigatorVM

@Composable
fun RadialAppNavigation (vm: RadialAppNavigatorVM){

    var selectionOffset: Offset? = null
    var color: Color

    if (vm.visibility){
        vm.offsets.forEachIndexed { index, it ->
            if (vm.selectionIndex == index){
                selectionOffset = it
                color = Color.White
            }
            else{
                color = if (index in vm.possibleSelections) Color.Gray else Color.Red
            }
            Box(
                modifier = Modifier
                    .offset { it.round() }
                    .size(5.dp)
                    .background(color)
            )
        }
    }

    FluidCursorValues.updateData(FluidCursorData(
        targetPosition = selectionOffset?:  if (vm.offsetFromCenter.x >= 0) vm.center else vm.center + vm.offsetFromCenter,
        snap = vm.offsetFromCenter.x > 0f,
        visibility = vm.visibility
    ))
}