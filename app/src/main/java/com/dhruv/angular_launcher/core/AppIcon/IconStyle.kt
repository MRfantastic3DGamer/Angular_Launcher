package com.dhruv.angular_launcher.core.AppIcon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class IconStyle(
    val size: Dp = 40.dp,
    val borderStrokeWidth: Dp = 5.dp,
    val borderColor: Color = Color.Gray,
    val cornerRadios: Dp = 5.dp,
    val backGroundColor: Color = Color.Black,
){
    companion object {
        fun fromString(string: String): IconStyle {
            val parts = string.split(",")
            return IconStyle(
                parts[0].toFloat().dp,
                parts[1].toFloat().dp,
                Color(parts[2].toFloat(),parts[3].toFloat(),parts[4].toFloat(),parts[5].toFloat()),
                parts[6].toFloat().dp,
                Color(parts[7].toFloat(),parts[8].toFloat(),parts[9].toFloat(),parts[10].toFloat())
            )
        }
    }

    override fun toString(): String {
        return "${size.value},${borderStrokeWidth.value},${borderColor.red},${borderColor.green},${borderColor.blue},${borderColor.alpha},${cornerRadios.value},${backGroundColor.red},${backGroundColor.green},${backGroundColor.blue},${backGroundColor.alpha}"
    }
}