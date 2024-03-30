package com.dhruv.angular_launcher.accessible_screen.data

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dhruv.angular_launcher.core.database.prefferences.values.PrefValues

object AccessibleScreenValues {
    private val _persistentData           = MutableLiveData(ScreenPersistentData())
    private val _data                     = MutableLiveData(ScreenData())

    val GetPersistentData: LiveData<ScreenPersistentData>
        get() = _persistentData
    val GetData: LiveData<ScreenData>
        get() = _data

    fun markPersistentDataDirty(){ _persistentData.value = createPersistentData() }
    fun updateScreenData(touchPos: Offset, touchDelta: Offset?){
        _data.value = ScreenData(
            touchPosition = touchPos,
            delta = touchDelta,
        )
    }

    private fun createPersistentData(): ScreenPersistentData {
        return ScreenPersistentData(
            firstCut = PrefValues.s_firstCut,
            secondCut = PrefValues.s_secondCut,
        )
    }
}