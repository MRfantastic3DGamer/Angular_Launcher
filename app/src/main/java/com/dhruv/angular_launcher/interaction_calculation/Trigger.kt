package com.dhruv.angular_launcher.interaction_calculation

import android.content.Context
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.AccessibleScreenTrigger(
    context: Context,
    openSettings: (Context)->Unit,
    calculate: Boolean,
): Modifier{
    return if (calculate)
        this
            .pointerInput(Unit){
                detectTapGestures(
                    onTap = { offset ->
                        TriggerFunctions.Tap(context, offset)
                    },
                    onLongPress = {
                        openSettings(context)
                    }
                )
            }
            .pointerInput(Unit){
                detectDragGestures(
                    onDragStart = {offset -> TriggerFunctions.DragStart(context, offset) },
                    onDrag = {change, dragAmount ->
                        change.consume()
                        TriggerFunctions.Drag(context, dragAmount)
                    },
                    onDragEnd = { TriggerFunctions.DragEnd(context) },
                    onDragCancel = { TriggerFunctions.DragEnd(context) },
                )
            }
    else
        this
}