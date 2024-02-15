package com.dhruv.angular_launcher.accessible_screen.data

import android.content.Context
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dhruv.angular_launcher.data.models.NavigationMode
import com.dhruv.angular_launcher.data.models.NavigationStage
import com.dhruv.angular_launcher.data.models.SelectionMode
import com.dhruv.angular_launcher.settings_module.prefferences.values.PrefValues
import com.dhruv.angular_launcher.utils.ScreenUtils

object ScreenValues {
    private val _persistentData           = MutableLiveData<ScreenPersistentData>()
    private val _data                     = MutableLiveData<ScreenData>()

    val GetPersistentData: LiveData<ScreenPersistentData>
        get() = _persistentData
    val GetData: LiveData<ScreenData>
        get() = _data

    fun markPersistentDataDirty(){ _persistentData.value = createPersistentData() }
    fun updateScreenData(
        nm: NavigationMode?,
        sm: SelectionMode?,
        ns: NavigationStage?
    ){
        _data.value = _data.value!!.copy(
            navigationMode = nm ?: _data.value!!.navigationMode,
            selectionMode = sm ?: _data.value!!.selectionMode,
            navigationStage = ns ?: _data.value!!.navigationStage,
        )
    }

    private fun createPersistentData(): ScreenPersistentData {
        return ScreenPersistentData(
            sliderWidth = ScreenUtils.fToDp(PrefValues.sl_width),
            sliderHeight = ScreenUtils.fToDp(PrefValues.sl_height),
            sliderInitialLocation = ScreenUtils.fToDp(PrefValues.sl_initialLocation),
            topPadding = PrefValues.sl_TopPadding.dp,
            bottomPadding = PrefValues.sl_DownPadding.dp,
            firstCut = PrefValues.sl_firstCut,
            secondCut = PrefValues.sl_secondCut,
            sliderMovementSpeed = PrefValues.sl_movementSpeed,
        )
    }
}