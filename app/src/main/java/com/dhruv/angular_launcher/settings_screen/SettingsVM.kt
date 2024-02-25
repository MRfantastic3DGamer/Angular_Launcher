package com.dhruv.angular_launcher.settings_screen

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dhruv.angular_launcher.database.prefferences.values.PrefValues
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SettingsVM : ViewModel() {
    var settingsOpened by mutableStateOf(false)

    val _values: MutableMap<String, MutableState<Any>?> = mutableMapOf()

    fun tryToGetState (key: String): MutableState<Any>? {
        if (_values.containsKey(key)){
            return _values[key]!!
        }

        try {
            val variable = PrefValues::class.java.getDeclaredField(key)
            variable.isAccessible = true
            _values[key] = mutableStateOf(variable.get(PrefValues))
            return _values[key]
        }catch (error: Throwable){
            println("no reflection for $key")
            return null
        }
    }

    suspend fun save(context: Context){
        return withContext(Dispatchers.IO){
            _values.keys.forEach {
                PrefValues.changedValuesMap[it] = _values[it]!!.value!!
            }
            PrefValues.save(context)
        }
    }
}