package com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data.RadialAppNavigatorValues
import com.dhruv.angular_launcher.accessible_screen.data.VibrationData
import com.dhruv.angular_launcher.database.apps_data.AppsIconsDataValues
import com.dhruv.angular_launcher.data.enums.SelectionMode
import com.dhruv.angular_launcher.data.models.IconStyle
import com.example.launcher.Drawing.DrawablePainter

class RadialAppNavigatorVM:ViewModel() {


    val groupId: Int by mutableStateOf(-1)
    var visibility by mutableStateOf(false)
    var selectionMode: SelectionMode by mutableStateOf(SelectionMode.NotSelected)

    var iconStyle by mutableStateOf(IconStyle())
    var enlargeSelectedIconBy by mutableStateOf(0f)
    var shouldBlur by mutableStateOf(false)
    var blurAmount by mutableStateOf(0f)
    var tint by mutableStateOf(Color.Black)

    // feel
    var vibration by mutableStateOf(VibrationData())

    var roundsStartingDistances: List<List<Float>> by mutableStateOf(listOf())
    var iconsPerRound: List<List<Int>> by mutableStateOf(listOf())

    var sliderPosY: Float by mutableStateOf(0f)
    var center: Offset by mutableStateOf(Offset(500f, 100f))
    var offsetFromCenter: Offset by mutableStateOf(Offset.Zero)

    var sliderSelection: String by mutableStateOf("@")
    var selectionIndex: Int by mutableIntStateOf(-1)
    var shouldSelectApp: Boolean by mutableStateOf(false)

    var appsIcons: Map<String, DrawablePainter> by mutableStateOf(emptyMap())

    init {
        AppsIconsDataValues.getAppsIcons.observeForever { if (it != null) appsIcons = it }
        RadialAppNavigatorValues.GetPersistentData.observeForever {

            println("updating p data")

            iconStyle = it.iconStyle
            enlargeSelectedIconBy = it.enlargeSelectedIconBy
            shouldBlur = it.shouldBlur
            blurAmount = it.blurAmount
            tint = it.tint

            // feel
            vibration = it.vibration

            roundsStartingDistances = it.roundStartingDistances
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
        }
    }
}