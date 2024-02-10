package com.dhruv.angular_launcher.interaction_calculation

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext

@Composable
fun Trigger(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Box(modifier = modifier
        .pointerInput(Unit){
            detectTapGestures(
                onTap = { offset ->
                    println("tapped")
                    TriggerFunctions.Tap(context, offset)
                },
            )
        }
        .pointerInput(Unit){
            detectDragGestures(
                onDragStart = {offset -> TriggerFunctions.DragStart(context, offset) },
                onDrag = {change, dragAmount ->
                    println("dragged")
                    change.consume()
                    TriggerFunctions.Drag(context, dragAmount) },
                onDragEnd = { TriggerFunctions.DragEnd() },
                onDragCancel = { TriggerFunctions.DragEnd() },
            )
        }
    )
}