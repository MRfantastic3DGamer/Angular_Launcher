package com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigationFunctions.getBestIndex
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigationFunctions.getPossibleIconIndeces
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data.RadialAppNavigatorValues

class RadialAppNavigatorVM:ViewModel() {

    var iconSize by mutableStateOf(0f)
    var enlargeSelectedIconBy by mutableStateOf(0f)
    var shouldBlur by mutableStateOf(false)
    var blurAmount by mutableStateOf(0f)
    var tint by mutableStateOf(Color.Black)

    // icons positioning generation options
    var option1 by mutableStateOf(RadialAppNavigationFunctions.IconCoordinatesGenerationInput())
    var option2 by mutableStateOf(RadialAppNavigationFunctions.IconCoordinatesGenerationInput())
    var option3 by mutableStateOf(RadialAppNavigationFunctions.IconCoordinatesGenerationInput())
    var option4 by mutableStateOf(RadialAppNavigationFunctions.IconCoordinatesGenerationInput())
    var option5 by mutableStateOf(RadialAppNavigationFunctions.IconCoordinatesGenerationInput())

    // feel
    var vibrateOnSelectionChange by mutableStateOf(false)
    var vibrationAmount by mutableStateOf(0f)
    var vibrationTime by mutableStateOf(0f)

    var roundsStartingDistances: List<List<Float>> by mutableStateOf(listOf())
    var iconsPerRound: List<List<Int>> by mutableStateOf(listOf())

    var center: Offset by mutableStateOf(Offset(500f, 100f))
    var numberOfElements: Int by mutableIntStateOf(10)
    var offsets: List<Offset> by mutableStateOf(listOf())
    var skips: List<Pair<Int,Int>> by mutableStateOf(listOf())
    var offsetFromCenter: Offset by mutableStateOf(Offset.Zero)
    var currentQualityIndex: Int by mutableIntStateOf(0)
    var possibleSelections: List<Int> by mutableStateOf(listOf())
    var selectionIndex: Int by mutableIntStateOf(-1)

    init {
        RadialAppNavigatorValues.GetPersistentData.observeForever {

            iconSize = it.iconSize
            enlargeSelectedIconBy = it.enlargeSelectedIconBy
            shouldBlur = it.shouldBlur
            blurAmount = it.blurAmount
            tint = it.tint

// icons positioning generation options
            option1 = it.option1
            option2 = it.option2
            option3 = it.option3
            option4 = it.option4
            option5 = it.option5

// feel
            vibrateOnSelectionChange = it.vibrateOnSelectionChange
            vibrationAmount = it.vibrationAmount
            vibrationTime = it.vibrationTime

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

            if (it.shouldSelectApp){
                possibleSelections = getPossibleIconIndeces(offsetFromCenter, iconsPerRound[currentQualityIndex], roundsStartingDistances[currentQualityIndex], skips).toList()

                selectionIndex = getBestIndex(center+offsetFromCenter, offsets, possibleSelections)
            }
            else{
                selectionIndex = -1
            }
        }
    }
}