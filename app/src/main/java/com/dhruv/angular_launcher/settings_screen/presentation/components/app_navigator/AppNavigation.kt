package com.dhruv.angular_launcher.settings_screen.presentation.components.app_navigator

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import com.dhruv.angular_launcher.accessible_screen.data.VibrationData
import com.dhruv.angular_launcher.data.models.IconCoordinatesGenerationInput
import com.dhruv.angular_launcher.data.models.IconStyle
import com.dhruv.angular_launcher.settings_screen.presentation.components.EntryData
import com.dhruv.angular_launcher.settings_screen.presentation.components.SettingsColumn
import com.dhruv.angular_launcher.settings_screen.presentation.components.SettingsColumnData
import com.dhruv.angular_launcher.settings_screen.presentation.components._SettingsArt

@Composable
fun AppNavigation (
    // look
    iconStyle: MutableState<IconStyle>,
    enlargeSelectedIconBy: MutableState<Float>,
    shouldBlur: MutableState<Boolean>,
    blurAmount: MutableState<Float>,
    tint: MutableState<Color>,

    // icons positioning generation options
    option1: MutableState<IconCoordinatesGenerationInput>,
    option2: MutableState<IconCoordinatesGenerationInput>,
    option3: MutableState<IconCoordinatesGenerationInput>,
    option4: MutableState<IconCoordinatesGenerationInput>,
    option5: MutableState<IconCoordinatesGenerationInput>,

    // feel
    vibrationData: MutableState<VibrationData>,
){
    LazyColumn(){
        item {
            // Display settings related to look
            SettingsColumn(
                data = SettingsColumnData("Look", null, listOf(
                    EntryData("Icon Size", iconStyle as MutableState<Any>, IconStyle::class.java),
                    EntryData("Enlarge Selected Icon By", enlargeSelectedIconBy as MutableState<Any>, Float::class.java),
                    EntryData("Should Blur", shouldBlur as MutableState<Any>, Boolean::class.java),
                    EntryData("Blur Amount", blurAmount as MutableState<Any>, Float::class.java),
                    EntryData("Tint", tint as MutableState<Any>, Color::class.java),
                )),
                entryForType = _SettingsArt.DefaultEntry
            )
        }
        item {
            // Display settings related to icon positioning generation options
            SettingsColumn(
                data = SettingsColumnData("Icons Positioning Generation Options", null, listOf(
                    EntryData("Option 1", option1 as MutableState<Any>, IconCoordinatesGenerationInput::class.java),
                    EntryData("Option 2", option2 as MutableState<Any>, IconCoordinatesGenerationInput::class.java),
                    EntryData("Option 3", option3 as MutableState<Any>, IconCoordinatesGenerationInput::class.java),
                    EntryData("Option 4", option4 as MutableState<Any>, IconCoordinatesGenerationInput::class.java),
                    EntryData("Option 5", option5 as MutableState<Any>, IconCoordinatesGenerationInput::class.java),
                )),
                entryForType = _SettingsArt.DefaultEntry
            )
        }
        item {
            // Display settings related to feel
            SettingsColumn(
                data = SettingsColumnData("Feel", null, listOf(
                    EntryData("Vibration Time", vibrationData as MutableState<Any>, VibrationData::class.java),
                )),
                entryForType = _SettingsArt.DefaultEntry
            )
        }
    }
}