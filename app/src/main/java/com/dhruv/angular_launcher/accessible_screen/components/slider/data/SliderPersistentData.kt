package com.dhruv.angular_launcher.accessible_screen.components.slider.data

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
data class SliderPersistentData(
    val width: Dp = 50.dp,
    val height: Dp = 500.dp,
    val elementsCount: Int = 25,
    val sidePadding: Float = 30f,
    val selectionRadios: Float = 100f,

    // constraints
    val topPadding: Float = 50f,
    val DownPadding: Float = 50f,

    // looks
    val triggerCurveEdgeCount:Int = 15,
    val selectionCurveOffset:Float = 50f,
    val shouldBlur:Boolean = false,
    val blurAmount:Float = 1f,
    val tint:Color = Color.Black,

    // feel
    val animationSpeed:Float = 1f,
    val movementSpeed:Float = 1f,
    val vibrateOnSelectionChange:Boolean = false,
    val vibrationAmount:Float = 1f,
    val vibrationTime:Float = 0f,
)
