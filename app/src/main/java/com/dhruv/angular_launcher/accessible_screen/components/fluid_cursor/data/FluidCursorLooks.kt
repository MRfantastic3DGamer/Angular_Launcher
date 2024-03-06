package com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data

import androidx.compose.ui.graphics.Color

data class FluidCursorLooks(
    val color: Color = Color.White,
    val freeRadius: Float = 0.07f,
    val appStuckRadius: Float = 0.15f,
    val sliderStuckRadius: Float = 0.15f,
){
    companion object {
        fun fromString(string: String): FluidCursorLooks {
            val parts = string.split(",").map { it.toDouble() }
            return try {
                val R = parts[0].toInt()
                val G = parts[1].toInt()
                val B = parts[2].toInt()
                val A = parts[3].toInt()
                FluidCursorLooks(
                    Color(R,G,B,A),
                    parts[4].toFloat(),
                    parts[5].toFloat(),
                    parts[6].toFloat(),
                )
            }catch (e: Throwable){
                println("couldn't get it")
                FluidCursorLooks()
            }
        }
    }

    override fun toString(): String {
        return "${color.red},${color.green},${color.blue},${color.alpha},$freeRadius,$appStuckRadius,$sliderStuckRadius"
    }
}