package com.dhruv.angular_launcher.settings_screen.presentation.components.apps

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.dhruv.angular_launcher.database.room.models.AppData

class AppsEditingVM(
    allApps: List<AppData>,
    val updateApp: (AppData)->Unit
): ViewModel() {
    fun saveApp() {
        if (appName.text.isBlank()) return
        selectedApp.value = selectedApp.value.copy(name = appName.text, visible = appVisible)
        updateApp(selectedApp.value)
    }

    val selectedApp = mutableStateOf(AppData())
    val apps = allApps.toMutableList()
    var appName by mutableStateOf(TextFieldValue())
    var appVisible = true
    var showPopup = mutableStateOf(false)
}