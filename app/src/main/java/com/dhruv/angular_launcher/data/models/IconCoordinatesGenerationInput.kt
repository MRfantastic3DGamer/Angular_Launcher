package com.dhruv.angular_launcher.data.models

data class IconCoordinatesGenerationInput(
    val startingRadius:Double = 250.0,
    val radiusDiff:Double = 150.0,
    val iconDistance:Double = 150.0,
    val rounds: Int = 20,
){
    companion object {
        fun fromString(string: String): IconCoordinatesGenerationInput {
            val parts = string.split(",")
            return IconCoordinatesGenerationInput(
                parts[0].toDouble(),
                parts[1].toDouble(),
                parts[2].toDouble(),
                parts[3].toInt()
            )
        }
    }

    override fun toString(): String {
        return "$startingRadius,$radiusDiff,$iconDistance,$rounds"
    }
}