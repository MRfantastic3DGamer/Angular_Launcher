package com.dhruv.angular_launcher.accessible_screen.components.slider

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.dhruv.angular_launcher.accessible_screen.components.app_label.AppLabelFunctions
import com.dhruv.angular_launcher.accessible_screen.components.app_label.data.AppLabelData
import com.dhruv.angular_launcher.accessible_screen.components.app_label.data.AppLabelValue
import com.dhruv.angular_launcher.accessible_screen.components.slider.SliderFunctions.GetSliderPositionY
import com.dhruv.angular_launcher.accessible_screen.components.slider.data.SliderValues
import com.dhruv.angular_launcher.utils.ScreenUtils

class SliderVM:ViewModel() {
    var height: Float by mutableStateOf(500f)
    var elementsCount: Int by mutableStateOf(0)
    var sidePadding: Float by mutableStateOf(0f)

    var visible: Boolean by mutableStateOf(false)
    var sliderPos: Offset by mutableStateOf(Offset.Zero)
    var selectionIndex: Int by mutableStateOf(0)
    var selectionPosY: Float by mutableStateOf(0f)
    var touchPos: Offset by mutableStateOf(Offset.Zero)

    init {
        SliderValues.GetPersistentData.observeForever {
            height = ScreenUtils.dpToF(it.height)
            elementsCount = it.elementsCount
            sidePadding = it.sidePadding
        }
        SliderValues.GetSliderData.observeForever {
            visible = it.visible
            touchPos = it.touchPos
            if (it.shouldUpdateOffset){
                val posY = GetSliderPositionY(touchPos.y, height, sliderPos.y)
                sliderPos = SliderFunctions.calculateSliderPosition(posY)

                val appLabelPosition = AppLabelFunctions.calculatePosition(300f, sliderPos.y, height, 20f)
                AppLabelValue.updatePos(appLabelPosition)
            }

            if (it.shouldUpdateSelection) {
                val selection = SliderFunctions.calculateCurrentSelection(
                    elementsCount,
                    height,
                    touchPos.y - sliderPos.y
                )
                selectionPosY = selection.posY
                selectionIndex = selection.index
            }

        }
    }
}