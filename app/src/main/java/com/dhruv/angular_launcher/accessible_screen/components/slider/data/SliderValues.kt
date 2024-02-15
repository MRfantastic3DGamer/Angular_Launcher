package com.dhruv.angular_launcher.accessible_screen.components.slider.data

import androidx.compose.ui.graphics.Path
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dhruv.angular_launcher.settings_module.prefferences.values.PrefValues
import com.dhruv.angular_launcher.utils.ScreenUtils

object SliderValues {
    private var persistentData = MutableLiveData(SliderPersistentData())
    private var sliderData = MutableLiveData(SliderData())
    private var path = MutableLiveData(Path())

    val GetPersistentData: LiveData<SliderPersistentData>
        get() = persistentData
    val GetSliderData: LiveData<SliderData>
        get() = sliderData

    private fun createPersistentData(): SliderPersistentData {
        return SliderPersistentData(
            height = ScreenUtils.fToDp(PrefValues.sl_height),
            width = ScreenUtils.fToDp(PrefValues.sl_width),
            triggerCurveEdgeCount = PrefValues.sl_TriggerCurveEdgeCount,
            elementsCount = elementsCount(),
        )
    }

    fun markPersistentDataDirty() { persistentData.value = createPersistentData() }
    fun updateSliderData(newData: SliderData) {
        sliderData.value = newData
    }
    private fun elementsCount(): Int { return 26 /*TODO*/ }
}