package com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigatorVM

@Composable
fun RadialAppNavigation (vm: RadialAppNavigatorVM){

    vm.offsets.forEachIndexed { index, it ->
        Box(
            modifier = Modifier
                .offset { it.round() }
                .size(5.dp)
                .background( if (vm.selectionIndex-1 != index) Color.Red else Color.White)
        )
    }

}