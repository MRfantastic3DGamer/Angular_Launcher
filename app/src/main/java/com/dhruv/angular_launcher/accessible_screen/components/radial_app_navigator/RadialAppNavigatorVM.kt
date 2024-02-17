package com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigationFunctions.getSelectedIconIndex
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data.RadialAppNavigatorValues

class RadialAppNavigatorVM:ViewModel() {
    var roundsStartingDistances: List<List<Float>> by mutableStateOf(listOf())
    var iconsPerRound: List<List<Int>> by mutableStateOf(listOf())

    var center: Offset by mutableStateOf(Offset(500f, 100f))
    var numberOfElements: Int by mutableStateOf(10)
    var offsets: List<Offset> by mutableStateOf(listOf())
    var skips: List<Pair<Int,Int>> by mutableStateOf(listOf())
    var offsetFromCenter: Offset by mutableStateOf(Offset.Zero)
    var currentQualityIndex: Int by mutableStateOf(0)
    var selectionIndex: Int by mutableStateOf(-1)

    init {
        RadialAppNavigatorValues.GetPersistentData.observeForever {
            roundsStartingDistances = it.roundStartingDistances
            iconsPerRound = it.iconsPerRound
        }
        RadialAppNavigatorValues.GetData.observeForever {
            center = it.center
            numberOfElements = RadialAppNavigatorValues.GetPersistentData.value?.numberOfElementsPerSelection?.get(it.currentSelectionIndex) ?: 0
            val iconPositionsCompute = RadialAppNavigationFunctions.getUsableOffsets(
                RadialAppNavigatorValues.GetPersistentData.value?.offsetsScales ?: listOf(),
                center,
                numberOfElements,
                it.sliderPositionY
            )
            offsets = iconPositionsCompute.offsets
            skips = iconPositionsCompute.skips
            currentQualityIndex = iconPositionsCompute.qualityIndex
            offsetFromCenter = it.offsetFromCenter

            selectionIndex = getSelectedIconIndex(offsetFromCenter, iconsPerRound[currentQualityIndex], roundsStartingDistances[currentQualityIndex], skips)
            println(selectionIndex)
        }
    }
}