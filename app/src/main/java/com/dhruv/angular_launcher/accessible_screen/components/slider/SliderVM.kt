package com.dhruv.angular_launcher.accessible_screen.components.slider

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.dhruv.angular_launcher.accessible_screen.components.app_label.AppLabelFunctions
import com.dhruv.angular_launcher.accessible_screen.components.app_label.data.AppLabelValue
import com.dhruv.angular_launcher.accessible_screen.components.slider.SliderFunctions.GetSliderPositionY
import com.dhruv.angular_launcher.accessible_screen.components.slider.data.SliderValues
import com.dhruv.angular_launcher.accessible_screen.data.VibrationData
import com.dhruv.angular_launcher.data.enums.SelectionMode
import com.dhruv.angular_launcher.utils.ScreenUtils

class SliderVM:ViewModel() {

    var selectionMode: SelectionMode by mutableStateOf(SelectionMode.NotSelected)
    var shouldUpdateSelection: Boolean by mutableStateOf(false)

    // main
    var height: Float by mutableStateOf(500f)
    var width: Float by mutableStateOf(100f)

    // constraints
    var TopPadding by mutableStateOf(50f)
    var DownPadding by mutableStateOf(50f)

    // looks
    var shouldBlur by mutableStateOf(false)
    var blurAmount by mutableStateOf(1f)
    var tint by mutableStateOf(Color.Black)

    // feel
    var vibrationData by mutableStateOf(VibrationData())

    var sidePadding: Float by mutableStateOf(0f)

    var visible: Boolean by mutableStateOf(false)
    var sliderPos: Offset by mutableStateOf(Offset.Zero)
    var prevSelectionIndex: Int by mutableStateOf(0)
    var selectionIndex: Int by mutableStateOf(0)
    var selectionPosY: Float by mutableStateOf(0f)
    var touchPos: Offset by mutableStateOf(Offset.Zero)

    init {
        SliderValues.GetPersistentData.observeForever {
            TopPadding = it.topPadding
            DownPadding = it.DownPadding
            shouldBlur = it.shouldBlur
            blurAmount = it.blurAmount
            tint = it.tint
            vibrationData = it.vibrationData

            height = ScreenUtils.dpToF(it.height)
            sidePadding = it.sidePadding
        }
        SliderValues.GetSliderData.observeForever {
            touchPos = it.touchPos
            selectionMode = it.selectionMode
            visible = it.visible
            shouldUpdateSelection = it.shouldUpdateSelection
            if (it.shouldUpdateOffset){
                val posY = GetSliderPositionY(touchPos.y, height, sliderPos.y)
                sliderPos = SliderFunctions.calculateSliderPosition(posY)

                val appLabelPosition = AppLabelFunctions.calculatePosition(300f, sliderPos.y, height, 20f)
                AppLabelValue.updatePos(appLabelPosition)
            }
        }
    }
}
