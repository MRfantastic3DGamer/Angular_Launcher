package com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object FluidCursorValues {
    private var persistentData = MutableLiveData(FluidCursorPersistentData(FluidCursorLooks(), 1f))
    private var data = MutableLiveData(FluidCursorData())

    var updateShaderPosition: (Float, Float) -> Unit = {x,y -> }

    val GetPersistentData: LiveData<FluidCursorPersistentData>
        get() = persistentData
    val GetData: LiveData<FluidCursorData>
        get() = data

    fun updateTouchPosition(pos: Offset) {
        data.value = data.value!!.copy(touchPos = pos)
        val target = getCursorTargetPosition()
        updateShaderPosition(target.x, target.y)
    }

    fun updatesFromAppsNavigator(selectionOffset: Offset?) {
        data.value = data.value!!.copy(selectedIconOffset = selectionOffset)
    }

    fun updatesFromSlider(selectionOffset: Offset?, isSliderInFocus: Boolean) {
        data.value = data.value!!.copy(selectedGroupOffset = selectionOffset, isSliderOnFocus = isSliderInFocus)
    }

    fun getCursorTargetPosition(): Offset {
        return data.value!!.selectedIconOffset?:
        if (data.value!!.isSliderOnFocus) data.value!!.selectedGroupOffset?: data.value!!.touchPos
        else data.value!!.touchPos
    }
}