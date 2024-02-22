package com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.presentation

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SnapSpec
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Path
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.FluidCursorFunctions.simulateFluid
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.FluidCursorVM
import com.dhruv.angular_launcher.utils.PathToShape

@Composable
fun FluidCursor (vm: FluidCursorVM){
//    vm.points = simulateFluid(vm.points, vm.targetPos, 50.0, 0.01, 0.5)
//    val myPath = Path()
//        .apply {
//            moveTo(0f, 0f)
//            vm.points.forEach { lineTo(it.x, it.y) }
//            close()
//        }
    val pos by animateOffsetAsState(targetValue = vm.targetPos, label = "cursor-pos", animationSpec = if (vm.snap) SnapSpec() else spring())
    if (vm.visibility){
        Box(
            modifier = Modifier
                .size(10.dp)
                .offset { (pos - Offset(5f, 5f)).round() }
                .background(Color.Cyan)
        )
    }
}