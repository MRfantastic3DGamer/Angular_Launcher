package com.dhruv.angular_launcher.interaction_calculation

import android.content.Context
import androidx.compose.ui.geometry.Offset
import com.dhruv.angular_launcher.accessible_screen.components.app_label.AppLabelFunctions
import com.dhruv.angular_launcher.accessible_screen.components.app_label.data.AppLabelData
import com.dhruv.angular_launcher.accessible_screen.components.app_label.data.AppLabelValue
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data.RadialAppNavigatorData
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data.RadialAppNavigatorValues
import com.dhruv.angular_launcher.accessible_screen.components.slider.SliderFunctions
import com.dhruv.angular_launcher.accessible_screen.components.slider.data.SliderData
import com.dhruv.angular_launcher.accessible_screen.components.slider.data.SliderValues
import com.dhruv.angular_launcher.accessible_screen.data.AccessibleScreenValues
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

                AccessibleScreenValues.updateScreenData(offset, null)
            }
            NavigationMode.Tap -> {
//                when (data.navigationStage) {
//                    NavigationStage.Inactive -> TODO()
//                    NavigationStage.ModeSelect -> TODO()
//                    NavigationStage.AppSelect -> TODO()
//                }
                AccessibleScreenValues.updateScreenData(offset, null)
            }
            NavigationMode.Gesture -> {}
        }
    }

    fun DragStart(context: Context, offset: Offset) {
        data.c_fingerPosition = offset

        AccessibleScreenValues.updateScreenData(data.c_fingerPosition, offset)
    }

    fun Drag(context: Context, dragAmount: Offset) {
        data.c_fingerPosition += dragAmount

        AccessibleScreenValues.updateScreenData(data.c_fingerPosition, dragAmount)
    }

    fun DragEnd() {
        AccessibleScreenValues.updateScreenData(data.c_fingerPosition, null)
    }
}