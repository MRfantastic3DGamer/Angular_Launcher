package com.dhruv.angular_launcher.core.database.room

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.icu.text.SimpleDateFormat
import android.media.AudioManager
import android.os.BatteryManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhruv.angular_launcher.accessible_screen.components.glsl_art.MyGLRenderer
import com.dhruv.angular_launcher.core.database.prefferences.PreferencesManager
import com.dhruv.angular_launcher.core.database.room.dao.ThemeDataDao
import com.dhruv.angular_launcher.core.database.room.models.ThemeData
import com.dhruv.angular_launcher.core.database.room.models.getCopy
import com.dhruv.angular_launcher.core.database.room.models.getShader
import com.dhruv.angular_launcher.core.database.room.models.setShader
import com.dhruv.angular_launcher.settings_screen.ThemeUIEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class ThemeDatabaseVM(
    private val themeDataDao: ThemeDataDao,
): ViewModel() {
    val themes = themeDataDao.getAllThemes()

    var currTheme by mutableStateOf(ThemeData())

    var renderer: MyGLRenderer? by mutableStateOf(null)

    fun prepareRenderer(resources: Resources){
        renderer = MyGLRenderer(resources, currTheme.getShader())
    }

    fun onUIInput(uiEvent: ThemeUIEvent){
        when (uiEvent) {
            is ThemeUIEvent.SaveShaderCode -> {
                viewModelScope.launch(Dispatchers.IO) {
                    currTheme.let {
                        themeDataDao.update(it.setShader(uiEvent.shaderData))
                        renderer = MyGLRenderer(uiEvent.resources, uiEvent.shaderData)
                    }
                }
            }
            is ThemeUIEvent.ApplyTheme -> {
                viewModelScope.launch(Dispatchers.IO) {
                    PreferencesManager.getInstance(uiEvent.context).saveData("theme_id", uiEvent.id)
                    currTheme = themeDataDao.getThemeById(uiEvent.id)
                    renderer = MyGLRenderer(uiEvent.resources, currTheme.getShader())
                }
            }
            is ThemeUIEvent.SaveTheme -> {
                viewModelScope.launch(Dispatchers.IO) {
                    themeDataDao.insert(uiEvent.data)
                    currTheme = uiEvent.data
                    renderer = MyGLRenderer(uiEvent.resources, uiEvent.data.getShader())
                }
            }
            is ThemeUIEvent.UpdateCurrentTheme -> {
                viewModelScope.launch(Dispatchers.IO) {
                    themeDataDao.update(uiEvent.data)
                    currTheme = uiEvent.data
                    renderer = MyGLRenderer(uiEvent.resources, uiEvent.data.getShader())
                }
            }
            is ThemeUIEvent.SaveCopyTheme -> {
                viewModelScope.launch(Dispatchers.IO) {
                    themeDataDao.insert(uiEvent.data.getCopy())
                    currTheme = uiEvent.data
                    renderer = MyGLRenderer(uiEvent.resources, uiEvent.data.getShader())
                }
            }
            is ThemeUIEvent.DeleteTheme -> {
                viewModelScope.launch(Dispatchers.IO) {

                    themeDataDao.delete(uiEvent.id)
                }
            }
        }
    }

    fun GetBattery(context: Context): Int {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val batLevel: Int = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        return batLevel
    }

    fun GetDateTime(): List<Int> {
        val sdf = SimpleDateFormat("dd:MM:yyyy:HH:mm:ss")
        val currentDateAndTime = sdf.format(Date())
        return currentDateAndTime.split(":").map { it.toInt() }
    }

    fun GetVolume(context: Context): Float {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        return currentVolume.toFloat() / maxVolume.toFloat()
    }

    fun GetDarkMode(context: Context): Int {
        val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) 1 else 0
    }
}