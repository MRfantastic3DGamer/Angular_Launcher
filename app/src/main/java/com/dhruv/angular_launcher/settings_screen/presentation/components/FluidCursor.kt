package com.dhruv.angular_launcher.settings_screen.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorLooks
import com.dhruv.angular_launcher.settings_module.EntryData
import com.dhruv.angular_launcher.settings_module.SettingsColumn
import com.dhruv.angular_launcher.settings_module.SettingsColumnData


@Composable
fun FluidCursor (
    // looks
    looks: MutableState<FluidCursorLooks>,
    // feel
    animationSpeed: MutableState<Float>,
){
    // Display settings related to looks
    SettingsColumn(
        data = SettingsColumnData("Looks", null, listOf(
            EntryData("Cursor Looks", looks as MutableState<Any>, FluidCursorLooks::class.java)
        )),
        entryForType = _SettingsArt.DefaultEntry
    )

    // Display settings related to feel
    SettingsColumn(
        data = SettingsColumnData("Feel", null, listOf(
            EntryData("Animation Speed", animationSpeed as MutableState<Any>, Float::class.java)
        )),
        entryForType = _SettingsArt.DefaultEntry
    )
}
