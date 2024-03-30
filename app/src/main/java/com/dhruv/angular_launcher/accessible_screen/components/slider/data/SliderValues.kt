package com.dhruv.angular_launcher.accessible_screen.components.slider.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dhruv.angular_launcher.core.database.prefferences.values.PrefValues

object SliderValues {
    private var persistentData = MutableLiveData(SliderPersistentData())
    private var sliderData = MutableLiveData(SliderData())

    val GetPersistentData: LiveData<SliderPersistentData>
        get() = persistentData
    val GetSliderData: LiveData<SliderData>
        get() = sliderData

    private fun createPersistentData(): SliderPersistentData {
        return SliderPersistentData(
            vibrationData = PrefValues.sl_vibration,
        )
    }

    fun markPersistentDataDirty() { persistentData.value = createPersistentData() }
    fun updateSliderData(newData: SliderData) {
        sliderData.value = newData
    }
}