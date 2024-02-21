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
import com.dhruv.angular_launcher.utils.ScreenUtils

class SliderVM:ViewModel() {


    // main
    var height: Float by mutableStateOf(500f)
    var width: Float by mutableStateOf(100f)

    // constraints
    var TopPadding by mutableStateOf(50f)
    var DownPadding by mutableStateOf(50f)

    // looks
    var TriggerCurveEdgeCount by mutableStateOf(15)
    var selectionCurveOffset by mutableStateOf(50f)
    var shouldBlur by mutableStateOf(false)
    var blurAmount by mutableStateOf(1f)
    var tint by mutableStateOf(Color.Black)

    // feel
    var animationSpeed by  mutableStateOf(1f)
    var movementSpeed by  mutableStateOf(1f)
    var vibrateOnSelectionChange by mutableStateOf(false)
    var vibrationAmount by mutableStateOf(1f)
    var vibrationTime by mutableStateOf(0f)

    var elementsCount: Int by mutableStateOf(0)
    var sidePadding: Float by mutableStateOf(0f)

    var visible: Boolean by mutableStateOf(false)
    var sliderPos: Offset by mutableStateOf(Offset.Zero)
    var selectionIndex: Int by mutableStateOf(0)
    var selectionPosY: Float by mutableStateOf(0f)
    var touchPos: Offset by mutableStateOf(Offset.Zero)

    init {
        SliderValues.GetPersistentData.observeForever {
            TopPadding = it.topPadding
            DownPadding = it.DownPadding
            TriggerCurveEdgeCount = it.triggerCurveEdgeCount
            selectionCurveOffset = it.selectionCurveOffset
            shouldBlur = it.shouldBlur
            blurAmount = it.blurAmount
            tint = it.tint
            animationSpeed = it.animationSpeed
            movementSpeed = it.movementSpeed
            vibrateOnSelectionChange = it.vibrateOnSelectionChange
            vibrationAmount = it.vibrationAmount
            vibrationTime = it.vibrationTime

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