package com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import com.dhruv.angular_launcher.accessible_screen.data.VibrationData

@Stable
data class RadialAppNavigatorPersistentData(

    val offsetsScales: List<List<Offset>> = listOf(),
    val iconsPerRound: List<List<Int>> = listOf(),
    val roundStartingDistances: List<List<Float>> = listOf(),
// feel
    val vibration: VibrationData = VibrationData(),
)