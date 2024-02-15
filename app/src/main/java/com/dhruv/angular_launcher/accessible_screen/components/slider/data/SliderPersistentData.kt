package com.dhruv.angular_launcher.accessible_screen.components.slider.data

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
data class SliderPersistentData(
    val width: Dp = 50.dp,
    val height: Dp = 500.dp,
    val elementsCount: Int = 25,
    val sidePadding: Float = 30f,
    val selectionRadios: Float = 100f,
    val triggerCurveEdgeCount: Int = 20,

    // TODO: appearance related values
)
