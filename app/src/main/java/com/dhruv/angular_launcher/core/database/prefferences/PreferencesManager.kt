package com.dhruv.angular_launcher.core.database.prefferences

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.graphics.Color
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorLooks
import com.dhruv.angular_launcher.accessible_screen.data.VibrationData
import com.dhruv.angular_launcher.core.appIcon.IconStyle
import com.dhruv.angular_launcher.core.resources.ShaderData
import com.dhruv.angular_launcher.data.models.IconCoordinatesGenerationScheme

class PreferencesManager private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE)

    companion object {
        @Volatile
        private var instance: PreferencesManager? = null
        fun getInstance(context: Context): PreferencesManager {
            return instance ?: synchronized(this) {
                instance ?: PreferencesManager(context).also { instance = it }
            }
        }
    }
    fun saveData(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }
    fun saveData(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }
    fun saveData(key: String, value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }
    fun saveData(key: String, value: IconCoordinatesGenerationScheme) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value.toString())
        editor.apply()
    }
    fun saveData(key: String, value: IconStyle) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value.toString())
        editor.apply()
    }
    fun saveData(key: String, value: Color) {
        val editor = sharedPreferences.edit()
        editor.putString(key, ColortoString(value))
        editor.apply()
    }
    fun saveData(key: String, value: FluidCursorLooks) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value.toString())
        editor.apply()
    }
    fun saveData(key: String, value: VibrationData) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value.toString())
        editor.apply()
    }
    fun saveData(key: String, value: ShaderData) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value.toString())
        editor.apply()
    }

    fun getData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }
    fun getData(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }
    fun getData(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }
    fun getData(key: String, defaultValue: Long): Long {
        return sharedPreferences.getString(key, defaultValue.toString())?.toLong() ?: defaultValue
    }
    fun getData(key: String, defaultValue: Float): Float {
        return sharedPreferences.getString(key, defaultValue.toString())?.toFloat() ?: defaultValue
    }
    fun getData(key: String, defaultValue: IconCoordinatesGenerationScheme): IconCoordinatesGenerationScheme {
        val storedValue = sharedPreferences.getString(key, defaultValue.toString())
        return IconCoordinatesGenerationScheme.fromString(storedValue ?: defaultValue.toString())
    }
    fun getData(key: String, defaultValue: IconStyle): IconStyle {
        val storedValue = sharedPreferences.getString(key, defaultValue.toString())
        return IconStyle.fromString(storedValue ?: defaultValue.toString())
    }
    fun getData(key: String, defaultValue: ShaderData): ShaderData {
        val storedValue = sharedPreferences.getString(key, defaultValue.toString())
        return ShaderData.fromString(storedValue ?: defaultValue.toString())
    }
    fun getData(key: String, defaultValue: Color): Color {
        val storedValue = sharedPreferences.getString(key, ColortoString(defaultValue))
        return Color.fromString(storedValue ?: defaultValue.toString())
    }
    fun getData(key: String, defaultValue: FluidCursorLooks): FluidCursorLooks {
        val storedValue = sharedPreferences.getString(key, defaultValue.toString())
        return FluidCursorLooks.fromString(storedValue ?: defaultValue.toString())
    }
    fun getData(key: String, defaultValue: VibrationData): VibrationData {
        val storedValue = sharedPreferences.getString(key, defaultValue.toString())
        return VibrationData.fromString(storedValue ?: defaultValue.toString())
    }

    fun Color.Companion.fromString(colorString: String): Color {
        val elements = colorString.split(',')
        return if (elements.size == 4) Color(elements[0].toFloat(),elements[1].toFloat(),elements[2].toFloat(),elements[3].toFloat()) else Color.Red
    }
    fun ColortoString(color: Color): String {
        return "${color.red},${color.green},${color.blue},${color.alpha}"
    }
}