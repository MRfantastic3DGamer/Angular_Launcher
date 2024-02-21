package com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigationFunctions

@Stable
data class RadialAppNavigatorPersistentData(
    val numberOfElementsPerSelection: List<Int> = listOf(50,10,4,4,8,20,35,25,20,19,70,5,10,4,4,8,20,35,25,20,19,70,15,10,4,4,8,20,35,25,20,19,70),
    val width: Float = 100f,

    val offsetsScales: List<List<Offset>> = listOf(),
    val iconsPerRound: List<List<Int>> = listOf(),
    val roundStartingDistances: List<List<Float>> = listOf(),

// look
    val iconSize: Float = 0f,
    val enlargeSelectedIconBy: Float = 0f,
    val shouldBlur: Boolean = false,
    val blurAmount: Float = 0f,
    val tint: Color = Color.Black,

// icons positioning generation options
    val option1: RadialAppNavigationFunctions.IconCoordinatesGenerationInput = RadialAppNavigationFunctions.IconCoordinatesGenerationInput(),
    val option2: RadialAppNavigationFunctions.IconCoordinatesGenerationInput = RadialAppNavigationFunctions.IconCoordinatesGenerationInput(),
    val option3: RadialAppNavigationFunctions.IconCoordinatesGenerationInput = RadialAppNavigationFunctions.IconCoordinatesGenerationInput(),
    val option4: RadialAppNavigationFunctions.IconCoordinatesGenerationInput = RadialAppNavigationFunctions.IconCoordinatesGenerationInput(),
    val option5: RadialAppNavigationFunctions.IconCoordinatesGenerationInput = RadialAppNavigationFunctions.IconCoordinatesGenerationInput(),

// feel
    val vibrateOnSelectionChange: Boolean = false,
    val vibrationAmount: Float = 0f,
    val vibrationTime: Float = 0f
)