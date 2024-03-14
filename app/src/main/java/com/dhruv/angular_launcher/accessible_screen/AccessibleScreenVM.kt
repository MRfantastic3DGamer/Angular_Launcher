package com.dhruv.angular_launcher.accessible_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dhruv.angular_launcher.accessible_screen.components.app_label.data.AppLabelValue
import com.dhruv.angular_launcher.accessible_screen.components.slider.data.SliderData
import com.dhruv.angular_launcher.accessible_screen.components.slider.data.SliderValues
import com.dhruv.angular_launcher.accessible_screen.data.AccessibleScreenValues
import com.dhruv.angular_launcher.core.database.prefferences.values.PrefValues
import com.dhruv.angular_launcher.data.enums.NavigationMode
import com.dhruv.angular_launcher.data.enums.NavigationStage
import com.dhruv.angular_launcher.data.enums.SelectionMode
import com.dhruv.angular_launcher.utils.ScreenUtils
import kotlin.math.absoluteValue

class AccessibleScreenVM(): ViewModel() {

    var navigationMode: NavigationMode by mutableStateOf(NavigationMode.NotSelected)
    var selectionMode: SelectionMode by mutableStateOf(SelectionMode.NotSelected)
    var navigationStage: NavigationStage by mutableStateOf(NavigationStage.SelectionModeSelection)

    var sliderVisibility: Boolean by mutableStateOf(false)
    var focusOnSlider: Boolean by mutableStateOf(false)

//    var

    init {
        AccessibleScreenValues.GetData.observeForever {

            val collisionQuality = 5f
            val sliderLeftBound: Float = ScreenUtils.fromRight( ScreenUtils.dpToF(SliderValues.GetPersistentData.value!!.width) )

            when (navigationStage) {
                NavigationStage.SelectionModeSelection -> {

                    // TODO: make it editable
                    val selectionChoice = arrayListOf(SelectionMode.ByAlphabet, SelectionMode.ByGroup, SelectionMode.ByGroup)

                    val screenHeight = ScreenUtils.screenHeight
                    val firstCutHeight = PrefValues.s_firstCut * screenHeight
                    val secondCutHeight = PrefValues.s_secondCut * screenHeight

                    if (it.touchPosition.x > sliderLeftBound){
                        sliderVisibility = true
                        focusOnSlider = true
                        selectionMode =
                            if (it.touchPosition.y < firstCutHeight){ selectionChoice[0] }
                            else if (it.touchPosition.y < secondCutHeight){ selectionChoice[1] }
                            else{ selectionChoice[2] }

                        navigationMode = if (it.delta == null) NavigationMode.Tap else NavigationMode.Gesture
                        navigationStage = NavigationStage.AppSelect
                    }
                }
                NavigationStage.AppSelect -> {
                    focusOnSlider = !(it.touchPosition.x < sliderLeftBound-collisionQuality || (it.delta != null && it.delta.x.absoluteValue*3 > it.delta.y.absoluteValue))

                    if (it.delta == null) {
                        sliderVisibility = false
                        navigationMode = NavigationMode.NotSelected
                        navigationStage = NavigationStage.SelectionModeSelection
                        if (!focusOnSlider) {
                            AppLabelValue.launchAppIfPossible()
                        }
                    }

                    SliderValues.updateSliderData(
                        SliderData(
                            visible = sliderVisibility,
                            shouldUpdateOffset = focusOnSlider,
                            shouldUpdateSelection = focusOnSlider,
                            touchPos = it.touchPosition,
                            selectionMode = selectionMode,
                        )
                    )
                }
            }
        }
    }
}