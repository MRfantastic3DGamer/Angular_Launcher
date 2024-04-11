package com.dhruv.angular_launcher.core.database.room

import android.content.ContentResolver
import android.content.res.Resources
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

class ThemeDatabaseVM(
    private val themeDataDao: ThemeDataDao,
    private val resources: Resources,
): ViewModel() {
    val themes = themeDataDao.getAllThemes()

    var currTheme by mutableStateOf(ThemeData())

    var renderer: MyGLRenderer? by mutableStateOf(null)

    fun prepareRenderer(contentResolver: ContentResolver){
        renderer = MyGLRenderer(contentResolver, resources, currTheme.getShader())
    }

    fun addData(key: String, value: Any) {
        if (currTheme.getShader().resourcesAsked.contains(key))
            renderer?.PrepareData(key, value)
    }

    fun onUIInput(uiEvent: ThemeUIEvent){
        when (uiEvent) {
            is ThemeUIEvent.SaveShaderToCurrentTheme -> {
                viewModelScope.launch(Dispatchers.IO) {
                    currTheme = currTheme.setShader(uiEvent.shaderData)
                    themeDataDao.update(currTheme)
                    prepareRenderer(uiEvent.contentResolver)
                }
            }
            is ThemeUIEvent.ApplyTheme -> {
                viewModelScope.launch(Dispatchers.IO) {
                    PreferencesManager.getInstance(uiEvent.context).saveData("theme_id", uiEvent.id)
                    currTheme = themeDataDao.getThemeById(uiEvent.id)
                    prepareRenderer(uiEvent.contentResolver)
                }
            }
            is ThemeUIEvent.SaveTheme -> {
                viewModelScope.launch(Dispatchers.IO) {
                    themeDataDao.insert(uiEvent.data)
                    currTheme = uiEvent.data
                    prepareRenderer(uiEvent.contentResolver)
                }
            }
            is ThemeUIEvent.UpdateCurrentTheme -> {
                viewModelScope.launch(Dispatchers.IO) {
                    themeDataDao.update(uiEvent.data)
                    currTheme = uiEvent.data
                    prepareRenderer(uiEvent.contentResolver)
                }
            }
            is ThemeUIEvent.SaveCopyTheme -> {
                viewModelScope.launch(Dispatchers.IO) {
                    themeDataDao.insert(uiEvent.data.getCopy())
                    currTheme = uiEvent.data
                    prepareRenderer(uiEvent.contentResolver)
                }
            }
            is ThemeUIEvent.DeleteTheme -> {
                viewModelScope.launch(Dispatchers.IO) {
                    themeDataDao.delete(uiEvent.id)
                }
            }
        }
    }
}