package com.dhruv.angular_launcher.interaction_calculation

import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.accessible_screen.components.app_label.AppLabelFunctions
import com.dhruv.angular_launcher.accessible_screen.components.app_label.data.AppLabelData
import com.dhruv.angular_launcher.accessible_screen.components.app_label.data.AppLabelValue
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data.RadialAppNavigatorData
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data.RadialAppNavigatorValues
import com.dhruv.angular_launcher.accessible_screen.components.slider.SliderFunctions
import com.dhruv.angular_launcher.accessible_screen.components.slider.data.SliderData
import com.dhruv.angular_launcher.accessible_screen.components.slider.data.SliderValues
import com.dhruv.angular_launcher.accessible_screen.data.ScreenValues
import com.dhruv.angular_launcher.data.models.NavigationMode
import com.dhruv.angular_launcher.interaction_calculation.data.TriggerData
import com.dhruv.angular_launcher.utils.ScreenUtils

object TriggerFunctions {
    val data: TriggerData = TriggerData()

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


    fun UpdateSliderPosition (offset: Offset){
        val height = ScreenUtils.dpToF(ScreenValues.GetPersistentData.value?.sliderHeight ?: 1000.dp)
        data.sl_c_topPositionY =
            if (offset.y < data.sl_c_topPositionY) offset.y
            else if (offset.y > data.sl_c_topPositionY + height) offset.y - height
            else data.sl_c_topPositionY
    }

    private fun UpdateScreenData (){
        val sliderPosition = SliderFunctions.calculateTriggerOffset(data.sl_c_topPositionY)
        val selection = SliderFunctions.calculateCurrentSelection(26, ScreenUtils.screenHeight, data.c_fingerPosition.y - sliderPosition.y)
        val appLabelPosition = AppLabelFunctions.getOffset(300f, data.sl_c_topPositionY,ScreenUtils.dpToF(SliderValues.GetPersistentData.value!!.height), 20f)

        SliderValues.updateSliderData(
            SliderData(
                sliderOffset = sliderPosition,
                touchPosY = data.c_fingerPosition.y - data.sl_c_topPositionY,
            )
        )
        RadialAppNavigatorValues.updateData(
            RadialAppNavigatorData(
                currentSelectionIndex = selection.index,
            )
        )
        AppLabelValue.updateData(AppLabelData(
            position = appLabelPosition
        ))
    }
}