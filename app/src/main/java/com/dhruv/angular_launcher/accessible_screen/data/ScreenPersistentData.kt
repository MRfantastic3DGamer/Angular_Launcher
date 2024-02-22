package com.dhruv.angular_launcher.accessible_screen.data

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * these default values don't matter they will be overwritten instantly
 */
@Stable
data class ScreenPersistentData(
    val sliderWidth: Dp = 50.dp,
    val sliderHeight: Dp = 500.dp,
    val sliderInitialLocation: Dp = 0.dp,
    val topPadding: Dp = 0.dp,
    val bottomPadding: Dp = 0.dp,
    val firstCut: Float = 0.33f,
    val secondCut: Float = 0.66f,
)