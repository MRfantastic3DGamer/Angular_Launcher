package com.dhruv.angular_launcher.utils

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * all the calculations should be done in px(int/float) and presented using Dp
 */
object ScreenUtils {
    var pixelDensity = 2.5f

    fun fToDp (f: Float): Dp = (f / pixelDensity).dp
    fun dpToF (dp: Dp): Float = (dp.value * pixelDensity)


    var screenWidth = 100f
    var screenHeight = 100f

    fun fromRight(f: Float): Float = screenWidth  - f
    fun fromRight(dp: Dp): Dp = fToDp(screenWidth) - dp
    fun fromDown(f: Float): Float  = screenHeight - f
    fun fromDown(dp: Dp): Dp  = fToDp(screenHeight) - dp
}