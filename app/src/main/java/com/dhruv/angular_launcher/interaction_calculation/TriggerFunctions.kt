package com.dhruv.angular_launcher.interaction_calculation

import android.content.Context
import androidx.compose.ui.geometry.Offset
import com.dhruv.angular_launcher.accessible_screen.data.ScreenData
import com.dhruv.angular_launcher.accessible_screen.data.ScreenPersistentData
import com.dhruv.angular_launcher.accessible_screen.data.ScreenValues
import com.dhruv.angular_launcher.data.models.NavigationMode
import com.dhruv.angular_launcher.utils.ScreenUtils

object TriggerFunctions {
    val data:TriggerData = TriggerData()
    var persistentData = ScreenPersistentData()

    init {
        ScreenValues.GetPersistentData.observeForever {
            persistentData = it
        }
    }

    fun Tap(context: Context, offset: Offset) {
        when (data.navigationMode) {
            NavigationMode.NotSelected -> {
                data.navigationMode = NavigationMode.Tap
                data.c_fingerPosition = offset
                UpdateSliderPosition(offset)
            }
            NavigationMode.Tap -> {
//                when (data.navigationStage) {
//                    NavigationStage.Inactive -> TODO()
//                    NavigationStage.ModeSelect -> TODO()
//                    NavigationStage.AppSelect -> TODO()
//                }
                UpdateSliderPosition(offset)
            }
            NavigationMode.Gesture -> {}
        }
        UpdateScreenData()
    }

    fun DragStart(context: Context, offset: Offset) {
        data.c_fingerPosition = offset

        UpdateSliderPosition(offset)
        UpdateScreenData()
    }

    fun Drag(context: Context, dragAmount: Offset) {
        data.c_fingerPosition += dragAmount

        UpdateSliderPosition(data.c_fingerPosition)
        UpdateScreenData()
    }

    fun DragEnd() {
        UpdateScreenData()
    }

    private fun UpdateScreenData (){
        ScreenValues.updateScreenData(ScreenData(
            touchLocation = data.c_fingerPosition.y,
            sliderTopPosition = data.sl_c_topPositionY,
            navigationMode = data.navigationMode,
            navigationStage = data.navigationStage,
            selectionMode = data.selectionMode
        ))
    }

    private fun UpdateSliderPosition (offset: Offset){
        val height = ScreenUtils.dpToF(persistentData.sliderHeight)
        data.sl_c_topPositionY =
            if (offset.y < data.sl_c_topPositionY) offset.y
            else if (offset.y > data.sl_c_topPositionY + height) offset.y - height
            else data.sl_c_topPositionY
    }
}