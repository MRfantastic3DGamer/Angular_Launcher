package com.dhruv.angular_launcher.settings_screen.presentation.components.slider

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.dhruv.angular_launcher.accessible_screen.data.VibrationData
import com.dhruv.angular_launcher.core.database.room.ThemeDatabase
import com.dhruv.angular_launcher.settings_screen.ThemeUIEvent
import com.dhruv.angular_launcher.settings_screen.presentation.components.EntryData
import com.dhruv.angular_launcher.settings_screen.presentation.components.H2
import com.dhruv.angular_launcher.settings_screen.presentation.components.LabelForEnum
import com.dhruv.angular_launcher.settings_screen.presentation.components.LabelForFloat
import com.dhruv.angular_launcher.settings_screen.presentation.components.SettingsColumn
import com.dhruv.angular_launcher.settings_screen.presentation.components.SettingsColumnData
import com.dhruv.angular_launcher.settings_screen.presentation.components._SettingsArt

@Composable
fun Slider (
    vibration: MutableState<VibrationData>,
) {
    val context = LocalContext.current
    val contentResolver = context.contentResolver
    val vm = remember { ThemeDatabase.getViewModel(context) }
    val state = vm.currTheme

    LazyColumn{
        item {
            LabelForFloat(key = "width", min = 10f, value = state.sliderWidth, max = 100f) {
                vm.onUIInput(ThemeUIEvent.UpdateCurrentTheme(contentResolver, state.copy(sliderWidth = it)))
            }
        }

        item {
            LabelForEnum(key = "height setting mode", value = state.heightMode, options = mapOf(
                0 to Pair("adaptive", "height set according to number of groups"),
                1 to Pair("constant", "have a constant height"),
            )) {
                vm.onUIInput( ThemeUIEvent.UpdateCurrentTheme(contentResolver, state.copy(heightMode = it)))
            }
        }

        item {
            when (state.heightMode) {
                0 -> {
                    LabelForFloat(key = "group slider height", min = 1f, value = state.perGroupSliderHeight, max = 100f) {
                        vm.onUIInput( ThemeUIEvent.UpdateCurrentTheme(contentResolver, state.copy(perGroupSliderHeight = it)))
                    }
                }
                1 -> {
                    LabelForFloat(key = "constant slider height", min = 10f, value = state.constantSliderHeight, max = 100f) {
                        vm.onUIInput( ThemeUIEvent.UpdateCurrentTheme(contentResolver, state.copy(constantSliderHeight = it)))
                    }
                }
                else -> {}
            }
        }

        item {
            H2(text = "Constraints")
        }
        item {
            LabelForFloat(key = "top limit", min = 0f, value = state.topSliderLimit, max = 100f) {
                vm.onUIInput( ThemeUIEvent.UpdateCurrentTheme(contentResolver, state.copy(topSliderLimit = it)))
            }
        }
        item {
            LabelForFloat(key = "bottom limit", min = 0f, value = state.downSliderLimit, max = 100f) {
                vm.onUIInput( ThemeUIEvent.UpdateCurrentTheme(contentResolver, state.copy(downSliderLimit = it)))
            }
        }

        item {
            // Display settings related to feel
            SettingsColumn(
                data = SettingsColumnData("Feel", null, listOf(
                    EntryData("Vibrate On Selection Change", vibration as MutableState<Any>, VibrationData::class.java),
                )),
                entryForType = _SettingsArt.DefaultEntry
            )
        }
    }
}
