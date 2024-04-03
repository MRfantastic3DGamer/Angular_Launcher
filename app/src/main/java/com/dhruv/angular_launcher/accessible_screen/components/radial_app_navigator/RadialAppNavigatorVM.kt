package com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data.RadialAppNavigatorValues
import com.dhruv.angular_launcher.accessible_screen.data.VibrationData
import com.dhruv.angular_launcher.core.database.apps_data.AppsIconsDataValues
import com.dhruv.angular_launcher.data.enums.SelectionMode
import com.example.launcher.Drawing.DrawablePainter

class RadialAppNavigatorVM():ViewModel() {

    var visibility by mutableStateOf(false)
    var selectionMode: SelectionMode by mutableStateOf(SelectionMode.NotSelected)

    // feel
    var vibration by mutableStateOf(VibrationData())

    var iconsPerRound: List<List<Int>> by mutableStateOf(listOf())

    var sliderHeight by mutableStateOf(0f)
    var sliderPosY: Float by mutableStateOf(0f)
    var center: Offset by mutableStateOf(Offset(500f, 100f))
    var offsetFromCenter: Offset by mutableStateOf(Offset.Zero)

    var sliderSelection: String by mutableStateOf("@")
    var prevSelectionIndex: Int by mutableStateOf(-1)
    var selectionIndex: Int by mutableIntStateOf(-1)
    var selectionAmount: Map<Int, Float> by mutableStateOf(emptyMap())
    var shouldSelectApp: Boolean by mutableStateOf(false)

    var appsIcons: Map<String, DrawablePainter> by mutableStateOf(emptyMap())

    init {
        AppsIconsDataValues.getAppsIcons.observeForever { if (it != null) appsIcons = it }
        RadialAppNavigatorValues.GetPersistentData.observeForever {

//            println("updating p data")

            // feel
            vibration = it.vibration

            iconsPerRound = it.iconsPerRound
        }
        RadialAppNavigatorValues.GetData.observeForever {
            sliderSelection = it.sliderSelection
            visibility = it.visibility
            center = it.center
            sliderPosY = it.sliderPositionY
            selectionMode = it.selectionMode

            offsetFromCenter = it.offsetFromCenter

            shouldSelectApp = it.shouldSelectApp

            sliderHeight = it.sliderHeight
        }
    }
}