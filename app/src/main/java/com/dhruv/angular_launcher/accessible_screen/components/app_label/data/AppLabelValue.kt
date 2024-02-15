package com.dhruv.angular_launcher.accessible_screen.components.app_label.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object AppLabelValue {
    private val data = MutableLiveData(AppLabelData())

    val GetData: LiveData<AppLabelData>
        get() = data

    fun updateData (new: AppLabelData){ data.value = new }
}