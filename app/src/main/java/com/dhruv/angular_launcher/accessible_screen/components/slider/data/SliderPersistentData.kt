package com.dhruv.angular_launcher.accessible_screen.components.slider.data

import androidx.compose.runtime.Stable
import com.dhruv.angular_launcher.accessible_screen.data.VibrationData

@Stable
data class SliderPersistentData(
//    val width: Dp = 50.dp,
//    val height: Dp = 500.dp,
//    val sidePadding: Float = 40f,
//    val selectionRadios: Float = 100f,
//
//    // constraints
//    val topPadding: Float = 50f,
//    val DownPadding: Float = 50f,
//
//    // looks
//    val triggerCurveEdgeCount:Int = 15,
//    val selectionCurveOffset:Float = 50f,
//    val shouldBlur:Boolean = false,
//    val blurAmount:Float = 1f,
//    val tint:Color = Color.Black,

    // feel
    val vibrationData: VibrationData = VibrationData()
)
