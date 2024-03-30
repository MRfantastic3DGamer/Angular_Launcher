package com.dhruv.angular_launcher.interaction_calculation

import android.content.Context
import androidx.compose.ui.geometry.Offset
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorValues
import com.dhruv.angular_launcher.accessible_screen.data.AccessibleScreenValues
import com.dhruv.angular_launcher.data.enums.NavigationMode
import com.dhruv.angular_launcher.haptics.HapticsHelper
import com.dhruv.angular_launcher.utils.MathUtils
import kotlin.math.absoluteValue

object TriggerFunctions {
    var navigationMode = NavigationMode.NotSelected
    var c_fingerPosition = Offset.Zero

    fun Tap(context: Context, offset: Offset) {
        when (navigationMode) {
            NavigationMode.NotSelected -> {
                navigationMode = NavigationMode.Tap
                c_fingerPosition = offset

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
        c_fingerPosition = offset

        AccessibleScreenValues.updateScreenData(c_fingerPosition, offset)
        FluidCursorValues.updateTouchPosition(c_fingerPosition)
    }

    fun Drag(context: Context, dragAmount: Offset) {
        c_fingerPosition += dragAmount
        HapticsHelper.setFingerSpeed(MathUtils.calculateDistance(dragAmount, Offset.Zero).absoluteValue)
        AccessibleScreenValues.updateScreenData(c_fingerPosition, dragAmount)
        FluidCursorValues.updateTouchPosition(c_fingerPosition)
    }

    fun DragEnd() {
        HapticsHelper.setFingerSpeed(0f)
        AccessibleScreenValues.updateScreenData(c_fingerPosition, null)
        FluidCursorValues.updateTouchPosition(c_fingerPosition)
    }
}