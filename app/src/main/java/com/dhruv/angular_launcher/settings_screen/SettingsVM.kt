package com.dhruv.angular_launcher.settings_screen

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dhruv.angular_launcher.settings_module.prefferences.values.PrefValues

class SettingsVM private constructor (
    allKeys: List<String>,
): ViewModel() {
    var settingsOpened by mutableStateOf(false)

    val values = allKeys.associateWith { key ->
        try {
            val variable = PrefValues::class.java.getDeclaredField(key)
            variable.isAccessible = true
            mutableStateOf(variable.get(PrefValues))
        }catch (error: Throwable){
            println("no reflection for $key")
            null
        }
    }

    fun save(context: Context){
        values.keys.forEach {
            PrefValues.changedValuesMap[it] = values[it]!!.value!!
        }
        PrefValues.save(context)
    }

    companion object {
        @Volatile
        private var instance: SettingsVM? = null

        fun getInstance(allKeys: List<String>): SettingsVM {
            return instance ?: synchronized(this) {
                instance ?: SettingsVM(allKeys).also { instance = it }
            }
        }
    }
}