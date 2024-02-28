package com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.dhruv.angular_launcher.accessible_screen.data.VibrationData
import com.dhruv.angular_launcher.core.AppIcon.IconStyle

@Stable
data class RadialAppNavigatorPersistentData(
    val numberOfElementsPerSelection: List<Int> = listOf(50,10,4,4,8,20,35,25,20,19,70,5,10,4,4,8,20,35,25,20,19,70,15,10,4,4,8,20,35,25,20,19,70),
    val width: Float = 100f,

    val offsetsScales: List<List<Offset>> = listOf(),
    val iconsPerRound: List<List<Int>> = listOf(),
    val roundStartingDistances: List<List<Float>> = listOf(),

// look
    val iconStyle: IconStyle = IconStyle(),
    val enlargeSelectedIconBy: Float = 0f,
    val shouldBlur: Boolean = false,
    val blurAmount: Float = 0f,
    val tint: Color = Color.Black,

// feel
    val vibration: VibrationData = VibrationData(),
)