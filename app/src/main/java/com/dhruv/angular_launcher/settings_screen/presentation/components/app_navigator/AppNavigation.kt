package com.dhruv.angular_launcher.settings_screen.presentation.components.app_navigator

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.dhruv.angular_launcher.accessible_screen.data.VibrationData
import com.dhruv.angular_launcher.core.database.room.ThemeDatabase
import com.dhruv.angular_launcher.core.database.room.models.getIconPositioningSchemes
import com.dhruv.angular_launcher.core.database.room.models.getIconStyles
import com.dhruv.angular_launcher.settings_screen.ThemeUIEvent
import com.dhruv.angular_launcher.settings_screen.presentation.components.EntryData
import com.dhruv.angular_launcher.settings_screen.presentation.components.LabelForIconStyle
import com.dhruv.angular_launcher.settings_screen.presentation.components.LabelForIconsPositioningScheme
import com.dhruv.angular_launcher.settings_screen.presentation.components.SettingsColumn
import com.dhruv.angular_launcher.settings_screen.presentation.components.SettingsColumnData
import com.dhruv.angular_launcher.settings_screen.presentation.components._SettingsArt

@Composable
fun AppNavigation (
    // feel
    vibrationData: MutableState<VibrationData>,
){

    val context = LocalContext.current
    val contentResolver = context.contentResolver
    val vm = remember { ThemeDatabase.getViewModel(context) }
    val theme = vm.currTheme
    // look
    val iconStyles = theme.getIconStyles()
    val schemes = theme.getIconPositioningSchemes()

    LazyColumn(){
        item {
            // Display settings related to look
            LabelForIconStyle(key = "unfocused icon style", style = iconStyles[0]) {
                vm.onUIInput(
                    ThemeUIEvent.UpdateCurrentTheme(
                        contentResolver,
                        theme.copy(navigationIconStyle = it.toString())
                    )
                )
            }
            LabelForIconStyle(key = "focused icon style", style = iconStyles[1]) {
                vm.onUIInput(
                    ThemeUIEvent.UpdateCurrentTheme(
                        contentResolver,
                        theme.copy(navigationSelectedIconStyle = it.toString())
                    )
                )
            }
        }
        item {
            LabelForIconsPositioningScheme(key = "option 1", state = schemes[0]) {
                vm.onUIInput(
                    ThemeUIEvent.UpdateCurrentTheme(
                        contentResolver,
                        theme.copy(iconsPositioningScheme1 = it.toString())
                    )
                )
            }
        }
        item {
            LabelForIconsPositioningScheme(key = "option 2", state = schemes[1]) {
                vm.onUIInput(
                    ThemeUIEvent.UpdateCurrentTheme(
                        contentResolver,
                        theme.copy(iconsPositioningScheme2 = it.toString())
                    )
                )
            }
        }
        item {
            LabelForIconsPositioningScheme(key = "option 3", state = schemes[2]) {
                vm.onUIInput(
                    ThemeUIEvent.UpdateCurrentTheme(
                        contentResolver,
                        theme.copy(iconsPositioningScheme3 = it.toString())
                    )
                )
            }
        }
        item {
            LabelForIconsPositioningScheme(key = "option 4", state = schemes[3]) {
                vm.onUIInput(
                    ThemeUIEvent.UpdateCurrentTheme(
                        contentResolver,
                        theme.copy(iconsPositioningScheme4 = it.toString())
                    )
                )
            }
        }
        item {
            LabelForIconsPositioningScheme(key = "option 5", state = schemes[4]) {
                vm.onUIInput(
                    ThemeUIEvent.UpdateCurrentTheme(
                        contentResolver,
                        theme.copy(iconsPositioningScheme5 = it.toString())
                    )
                )
            }
        }
        item {
            // Display settings related to feel
            SettingsColumn(
                data = SettingsColumnData("Feel", null, listOf(
                    EntryData("vibrate on selection change", vibrationData as MutableState<Any>, VibrationData::class.java),
                )),
                entryForType = _SettingsArt.DefaultEntry
            )
        }
    }
}