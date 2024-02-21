package com.dhruv.angular_launcher.settings_screen.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigationFunctions
import com.dhruv.angular_launcher.settings_module.EntryData
import com.dhruv.angular_launcher.settings_module.SettingsColumn
import com.dhruv.angular_launcher.settings_module.SettingsColumnData

@Composable
fun AppNavigation (
    // look
    iconSize: MutableState<Float>,
    enlargeSelectedIconBy: MutableState<Float>,
    shouldBlur: MutableState<Boolean>,
    blurAmount: MutableState<Float>,
    tint: MutableState<Color>,

    // icons positioning generation options
    option1: MutableState<RadialAppNavigationFunctions.IconCoordinatesGenerationInput>,
    option2: MutableState<RadialAppNavigationFunctions.IconCoordinatesGenerationInput>,
    option3: MutableState<RadialAppNavigationFunctions.IconCoordinatesGenerationInput>,
    option4: MutableState<RadialAppNavigationFunctions.IconCoordinatesGenerationInput>,
    option5: MutableState<RadialAppNavigationFunctions.IconCoordinatesGenerationInput>,

    // feel
    vibrateOnSelectionChange: MutableState<Boolean>,
    vibrationAmount: MutableState<Float>,
    vibrationTime: MutableState<Float>,
){
    // Display settings related to look
    SettingsColumn(
        data = SettingsColumnData("Look", null, listOf(
            EntryData("Icon Size", iconSize as MutableState<Any>, Float::class.java),
            EntryData("Enlarge Selected Icon By", enlargeSelectedIconBy as MutableState<Any>, Float::class.java),
            EntryData("Should Blur", shouldBlur as MutableState<Any>, Boolean::class.java),
            EntryData("Blur Amount", blurAmount as MutableState<Any>, Float::class.java),
            EntryData("Tint", tint as MutableState<Any>, Color::class.java),
        )),
        entryForType = _SettingsArt.DefaultEntry
    )

    // Display settings related to icon positioning generation options
    SettingsColumn(
        data = SettingsColumnData("Icons Positioning Generation Options", null, listOf(
            EntryData("Option 1", option1 as MutableState<Any>, RadialAppNavigationFunctions.IconCoordinatesGenerationInput::class.java),
            EntryData("Option 2", option2 as MutableState<Any>, RadialAppNavigationFunctions.IconCoordinatesGenerationInput::class.java),
            EntryData("Option 3", option3 as MutableState<Any>, RadialAppNavigationFunctions.IconCoordinatesGenerationInput::class.java),
            EntryData("Option 4", option4 as MutableState<Any>, RadialAppNavigationFunctions.IconCoordinatesGenerationInput::class.java),
            EntryData("Option 5", option5 as MutableState<Any>, RadialAppNavigationFunctions.IconCoordinatesGenerationInput::class.java),
        )),
        entryForType = _SettingsArt.DefaultEntry
    )

    // Display settings related to feel
    SettingsColumn(
        data = SettingsColumnData("Feel", null, listOf(
            EntryData("Vibrate On Selection Change", vibrateOnSelectionChange as MutableState<Any>, Boolean::class.java),
            EntryData("Vibration Amount", vibrationAmount as MutableState<Any>, Float::class.java),
            EntryData("Vibration Time", vibrationTime as MutableState<Any>, Float::class.java),
        )),
        entryForType = _SettingsArt.DefaultEntry
    )
}