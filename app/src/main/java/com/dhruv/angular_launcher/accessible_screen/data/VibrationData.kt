package com.dhruv.angular_launcher.accessible_screen.data

data class VibrationData(
    val active: Boolean = false,
    val scale: Float = 1f,
    val time: Float = 1f,
) {
    companion object {
        fun fromString(string: String): VibrationData {
            val parts = string.split(",")
            return VibrationData(
                parts[0].toBoolean(),
                parts[1].toFloat(),
                parts[2].toFloat()
            )
        }
    }

    override fun toString(): String {
        return "$active,$scale,$time"
    }
}