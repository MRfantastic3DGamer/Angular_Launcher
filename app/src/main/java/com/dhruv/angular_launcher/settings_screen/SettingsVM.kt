package com.dhruv.angular_launcher.settings_screen

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhruv.angular_launcher.accessible_screen.components.app_label.data.AppLabelValue
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorValues
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data.RadialAppNavigatorValues
import com.dhruv.angular_launcher.accessible_screen.components.slider.data.SliderValues
import com.dhruv.angular_launcher.accessible_screen.data.AccessibleScreenValues
import com.dhruv.angular_launcher.core.database.prefferences.values.PrefValues
import com.dhruv.angular_launcher.haptics.HapticsHelper
import com.dhruv.angular_launcher.settings_screen.data.SettingsTab
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsVM : ViewModel() {
    var settingsOpened by mutableStateOf(false)
    var selectedTab by mutableStateOf(SettingsTab.Theme)

    val _values: MutableMap<String, MutableState<Any>?> = mutableMapOf()


    fun tryToGetState (key: String): MutableState<Any>? {
        if (_values.containsKey(key)){
            return _values[key]!!
        }

        return try {
            val variable = PrefValues::class.java.getDeclaredField(key)
            variable.isAccessible = true
            _values[key] = mutableStateOf(variable.get(PrefValues))
            _values[key]
        }catch (error: Throwable){
            println("no reflection for $key")
            null
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
        HapticsHelper.toggleSettingsHaptic(context)
        AppLabelValue.updatePackageState("-@")
    }

    fun openSettings(context: Context, tab: SettingsTab){
        selectedTab = tab
        openSettings(context)
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
        HapticsHelper.toggleSettingsHaptic(context)
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