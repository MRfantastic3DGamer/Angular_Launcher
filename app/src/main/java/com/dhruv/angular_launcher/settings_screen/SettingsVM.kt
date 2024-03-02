package com.dhruv.angular_launcher.settings_screen

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorValues
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data.RadialAppNavigatorValues
import com.dhruv.angular_launcher.accessible_screen.components.slider.data.SliderValues
import com.dhruv.angular_launcher.accessible_screen.data.AccessibleScreenValues
import com.dhruv.angular_launcher.core.database.prefferences.values.PrefValues
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
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

    fun openSettings(context: Context){
        viewModelScope.launch {
            async { save(context) }.await().also {
                async { PrefValues.loadAllValues(context) }.await().also {
                    AccessibleScreenValues.markPersistentDataDirty()
                    SliderValues.markPersistentDataDirty()
                    RadialAppNavigatorValues.markPersistentDataDirty()
                    FluidCursorValues.markPersistentDataDirty()
                    settingsOpened = true
                }
            }
        }
    }

    fun exitSettings(context: Context){
        viewModelScope.launch {
            async { save(context) }.await().also {
                async { PrefValues.loadAllValues(context) }.await().also {
                    AccessibleScreenValues.markPersistentDataDirty()
                    SliderValues.markPersistentDataDirty()
                    RadialAppNavigatorValues.markPersistentDataDirty()
                    FluidCursorValues.markPersistentDataDirty()
                    settingsOpened = false
                }
            }
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