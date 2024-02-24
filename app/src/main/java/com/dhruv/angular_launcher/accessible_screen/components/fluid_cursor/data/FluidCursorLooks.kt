package com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data

import androidx.compose.ui.graphics.Color

data class FluidCursorLooks(
    val tint: Color = Color.White
){
    companion object {
        fun fromString(string: String): FluidCursorLooks {
            val parts = string.split(",").map { it.toDouble() }
            return try {
                println(parts)
                val R = parts[0].toInt()
                val G = parts[1].toInt()
                val B = parts[2].toInt()
                val A = parts[3].toInt()
                FluidCursorLooks(Color(R,G,B,A))
            }catch (e: Throwable){
                println("couldn't get it")
                FluidCursorLooks()
            }

        }
    }

    override fun toString(): String {
        return "${tint.red},${tint.green},${tint.blue},${tint.alpha}"
    }
}