package com.dhruv.angular_launcher.accessible_screen.components.app_label

import androidx.compose.ui.geometry.Offset

object AppLabelFunctions {
    fun getOffset (height: Float, sliderPosY: Float, sliderHeight: Float, padding: Float): Offset {
        if (sliderPosY > height+padding) {
            return Offset(0f, sliderPosY - height - padding)
        }
        else {
            println(sliderPosY + sliderHeight + padding)
            return Offset(0f, sliderPosY + sliderHeight + padding)
        }
    }
}