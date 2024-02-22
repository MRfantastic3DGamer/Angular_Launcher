package com.dhruv.angular_launcher.accessible_screen.components.slider.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dhruv.angular_launcher.settings_module.prefferences.values.PrefValues
import com.dhruv.angular_launcher.utils.ScreenUtils

object SliderValues {
    private var persistentData = MutableLiveData(SliderPersistentData())
    private var sliderData = MutableLiveData(SliderData())

    val GetPersistentData: LiveData<SliderPersistentData>
        get() = persistentData
    val GetSliderData: LiveData<SliderData>
        get() = sliderData

    private fun createPersistentData(): SliderPersistentData {
        return SliderPersistentData(
            height = ScreenUtils.fToDp(PrefValues.sl_height),
            width = ScreenUtils.fToDp(PrefValues.sl_width),
            blurAmount = PrefValues.sl_blurAmount,
            DownPadding = PrefValues.sl_downPadding,
            sidePadding = PrefValues.sl_selectionCurveOffset,
            selectionCurveOffset = PrefValues.sl_selectionCurveOffset,
            selectionRadios = PrefValues.sl_selectionRadios,
            shouldBlur = PrefValues.sl_shouldBlur,
            tint = PrefValues.sl_tint,
            topPadding = PrefValues.sl_topPadding,
            triggerCurveEdgeCount = PrefValues.sl_triggerCurveEdgeCount,
            vibrateOnSelectionChange = PrefValues.sl_vibrateOnSelectionChange,
            vibrationAmount = PrefValues.sl_vibrationAmount,
            vibrationTime = PrefValues.sl_vibrationTime,
        )
    }

    fun markPersistentDataDirty() { persistentData.value = createPersistentData() }
    fun updateSliderData(newData: SliderData) {
        sliderData.value = newData
    }
}