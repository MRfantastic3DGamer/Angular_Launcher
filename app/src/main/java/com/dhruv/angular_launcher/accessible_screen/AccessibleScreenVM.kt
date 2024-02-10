package com.dhruv.angular_launcher.accessible_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dhruv.angular_launcher.accessible_screen.data.ScreenData
import com.dhruv.angular_launcher.accessible_screen.data.ScreenPersistentData
import com.dhruv.angular_launcher.accessible_screen.data.ScreenValues

class AccessibleScreenVM(): ViewModel() {
    var persistentData by mutableStateOf(ScreenPersistentData())
    var screenData by mutableStateOf(ScreenData())

    init {
        ScreenValues.GetPersistentData.observeForever { persistentData = it }
        ScreenValues.GetData.observeForever { screenData = it }
    }
}