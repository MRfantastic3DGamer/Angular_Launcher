package com.dhruv.angular_launcher.accessible_screen.components.slider

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.dhruv.angular_launcher.accessible_screen.components.slider.data.SliderValues
import com.dhruv.angular_launcher.accessible_screen.data.VibrationData
import com.dhruv.angular_launcher.data.enums.SelectionMode
import com.dhruv.angular_launcher.settings_screen.data.SettingsTab

class SliderVM(
    private val openSettings: (Context, SettingsTab) -> Unit
) : ViewModel() {
    fun goToEditGroups(context: Context) {
        openSettings(context, SettingsTab.Groups)
    }

    var selectionMode: SelectionMode by mutableStateOf(SelectionMode.NotSelected)
    var shouldUpdateSelection: Boolean by mutableStateOf(false)

    // feel
    var vibrationData by mutableStateOf(VibrationData())

    var visible: Boolean by mutableStateOf(false)
    var sliderPos: Offset by mutableStateOf(Offset.Zero)
    var prevSelectionIndex: Int by mutableStateOf(0)
    var selectionIndex: Int by mutableStateOf(0)
    var selectionPosY: Float by mutableStateOf(0f)
    var touchPos: Offset by mutableStateOf(Offset.Zero)

    var moveToSettingsValue by mutableFloatStateOf(0f)

    private var updateTrigger: Boolean by mutableStateOf(false)
    fun UpdateTrigger(): Boolean{
        if (updateTrigger){
            updateTrigger = false
            return true
        }
        return false
    }

    init {
        SliderValues.GetPersistentData.observeForever {
            vibrationData = it.vibrationData
        }
        SliderValues.GetSliderData.observeForever {
            touchPos = it.touchPos
            selectionMode = it.selectionMode
            visible = it.visible
            shouldUpdateSelection = it.shouldUpdateSelection
            updateTrigger = it.shouldUpdateOffset
        }
    }
}