package com.dhruv.angular_launcher.settings_screen.presentation.components.cursor

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorLooks
import com.dhruv.angular_launcher.settings_screen.presentation.components.EntryData
import com.dhruv.angular_launcher.settings_screen.presentation.components.SettingsColumn
import com.dhruv.angular_launcher.settings_screen.presentation.components.SettingsColumnData
import com.dhruv.angular_launcher.settings_screen.presentation.components._SettingsArt


@Composable
fun FluidCursor (
    looks: MutableState<FluidCursorLooks>,
    animationSpeed: MutableState<Float>,
){
    LazyColumn(){
        item {
            // Display settings related to looks
            SettingsColumn(
                data = SettingsColumnData("Looks", null, listOf(
                    EntryData("Cursor Looks", looks as MutableState<Any>, FluidCursorLooks::class.java)
                )),
                entryForType = _SettingsArt.DefaultEntry
            )
        }

        item {
            // Display settings related to feel
            SettingsColumn(
                data = SettingsColumnData("Feel", null, listOf(
                    EntryData("Animation Speed", animationSpeed as MutableState<Any>, Float::class.java)
                )),
                entryForType = _SettingsArt.DefaultEntry
            )
        }
    }
}
