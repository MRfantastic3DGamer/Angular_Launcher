package com.dhruv.angular_launcher.settings_screen.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.dhruv.angular_launcher.settings_module.SettingsColumn
import com.dhruv.angular_launcher.settings_module.SettingsColumnData
import com.dhruv.angular_launcher.settings_module.EntryData

@Composable
fun Slider (
    // main
    width: MutableState<Float>,
    height:  MutableState<Float>,
    // constraints
    topPadding:  MutableState<Float>,
    downPadding:  MutableState<Float>,
    // looks
    triggerCurveEdgeCount:  MutableState<Int>,
    selectionCurveOffset:  MutableState<Float>,
    shouldBlur: MutableState<Boolean>,
    blurAmount: MutableState<Float>,
    tint: MutableState<Color>,
    // feel
    vibrateOnSelectionChange: MutableState<Boolean>,
    vibrationAmount: MutableState<Float>,
    vibrationTime: MutableState<Float>,
) {
    LazyColumn(){
        item {
            // Display settings related to main properties
            SettingsColumn(
                data = SettingsColumnData("Main", null, listOf(
                    EntryData("Width", width as MutableState<Any>, Float::class.java),
                    EntryData("Height", height as MutableState<Any>, Float::class.java),
                )),
                entryForType = _SettingsArt.DefaultEntry
            )
        }

        item {
            // Display settings related to constraints
            SettingsColumn(
                data = SettingsColumnData("Constraints", null, listOf(
                    EntryData("Top Padding", topPadding as MutableState<Any>, Float::class.java),
                    EntryData("Down Padding", downPadding as MutableState<Any>, Float::class.java),
                )),
                entryForType = _SettingsArt.DefaultEntry
            )
        }

        item {
            // Display settings related to looks
            SettingsColumn(
                data = SettingsColumnData("Looks", null, listOf(
                    EntryData("Trigger Curve Edge Count", triggerCurveEdgeCount as MutableState<Any>, Int::class.java),
                    EntryData("Selection Curve Offset", selectionCurveOffset as MutableState<Any>, Float::class.java),
                    EntryData("Should Blur", shouldBlur as MutableState<Any>, Boolean::class.java),
                    EntryData("Blur Amount", blurAmount as MutableState<Any>, Float::class.java),
                    EntryData("Tint", tint as MutableState<Any>, Color::class.java),
                )),
                entryForType = _SettingsArt.DefaultEntry
            )
        }

        item {
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
    }
}
