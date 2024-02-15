package com.dhruv.angular_launcher.settings_module.prefferences

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.utils.ScreenUtils

class PreferencesManager private constructor(context: Context) {

    private val sharedPreferencesCache: MutableMap<String, String> = mutableMapOf()

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

    fun getData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }
    fun getData(key: String, defaultValue: Int): Int {
        return sharedPreferences.getString(key, defaultValue.toString())?.toIntOrNull() ?: defaultValue
    }
    fun getData(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getString(key, defaultValue.toString()).toBoolean() ?: defaultValue
    }
    fun getData(key: String, defaultValue: Long): Long {
        return sharedPreferences.getString(key, defaultValue.toString())?.toLong() ?: defaultValue
    }
    fun getData(key: String, defaultValue: Float): Float {
        return sharedPreferences.getString(key, defaultValue.toString())?.toFloat() ?: defaultValue
    }
}

inline fun <reified T : Any> castString(value: String): T? {
    return when (T::class) {
        Int::class -> value.toIntOrNull()
        Double::class -> value.toDoubleOrNull()
        Float::class -> value.toFloatOrNull()
        Dp::class -> value.toIntOrNull()?.dp ?: 0.dp
        // Add other supported types as needed
        else -> null
    } as? T
}