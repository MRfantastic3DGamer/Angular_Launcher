package com.dhruv.angular_launcher.accessible_screen.components.app_label.data

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object AppLabelValue {
    private val data = MutableLiveData(AppLabelData())

    val GetData: LiveData<AppLabelData>
        get() = data

    fun updatePos (pos: Offset){ data.value = data.value!!.copy(position = pos)}
    fun updateText (text: String){ data.value = data.value!!.copy(appName = text)}
    fun launchAppIfPossible(): Boolean {
        println("launched app ${data.value!!.appName}")
        return true
    }
}