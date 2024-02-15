package com.dhruv.angular_launcher.accessible_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dhruv.angular_launcher.accessible_screen.data.ScreenData
import com.dhruv.angular_launcher.accessible_screen.data.ScreenPersistentData
import com.dhruv.angular_launcher.accessible_screen.data.ScreenValues
import com.dhruv.angular_launcher.data.models.NavigationMode
import com.dhruv.angular_launcher.data.models.NavigationStage
import com.dhruv.angular_launcher.data.models.SelectionMode

class AccessibleScreenVM(): ViewModel() {

    var navigationMode: NavigationMode by mutableStateOf(NavigationMode.NotSelected)
    var selectionMode: SelectionMode by mutableStateOf(SelectionMode.NotSelected)
    var navigationStage: NavigationStage by mutableStateOf(NavigationStage.ModeSelect)

    init {
        ScreenValues.GetData.observeForever {
            navigationMode = it.navigationMode
            selectionMode = it.selectionMode
            navigationStage = it.navigationStage
        }
    }
}