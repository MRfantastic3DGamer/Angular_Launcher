package com.dhruv.angular_launcher.accessible_screen.components.app_label

import androidx.compose.ui.geometry.Offset

object AppLabelFunctions {
    fun calculatePosition (height: Float, sliderPosY: Float, sliderHeight: Float, padding: Float): Offset {
        return if (sliderPosY > height+padding) {
            Offset(0f, sliderPosY - height - padding)
        } else {
            Offset(0f, sliderPosY + sliderHeight + padding)
        }
    }
}