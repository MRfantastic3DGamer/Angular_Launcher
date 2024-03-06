package com.dhruv.angular_launcher.debug

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round

@Composable
fun DebugLayer (vm: DebugLayerVM){
    Box (
        Modifier
            .fillMaxSize()
    ){
        LazyColumn(){
            items(vm.strings){
                Text(text = it, color = Color.White)
            }
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            for (pair in vm.lines) {
                val (start, end) = pair
                drawLine(
                    color = Color.White,
                    start = start,
                    end = end,
                    strokeWidth = 20f,
                )
            }

        }
        for (point in vm.points) {
            Box(
                Modifier
                    .offset { point.round() }
                    .size(10.dp)
                    .background(Color.White)
            )
        }
    }
}