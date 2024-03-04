package com.dhruv.angular_launcher.accessible_screen.components.app_label.data

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


object AppLabelValue {
    private val data = MutableLiveData(AppLabelData())

    val GetData: LiveData<AppLabelData>
        get() = data

    private val launchTrigger = MutableLiveData(false)

    fun updatePos (pos: Offset){ data.value = data.value!!.copy(position = pos)}
    fun updatePackageState (text: String){ data.value = data.value!!.copy(appPackage = text)}
    fun launchAppIfPossible() {
        launchTrigger.value = true
    }
    fun useLaunchTrigger(): Boolean {
        if (launchTrigger.value == true){
            launchTrigger.value = false
            return true
        }
        return false
    }
}