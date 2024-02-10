package com.dhruv.angular_launcher.accessible_screen.data

import android.content.Context
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dhruv.angular_launcher.data.values.PrefValues
import java.security.AccessControlContext

object ScreenValues {
    private val _persistentData           = MutableLiveData<ScreenPersistentData>()

    private val _data                     = MutableLiveData<ScreenData>()

    val GetPersistentData: LiveData<ScreenPersistentData>
        get() = _persistentData
    val GetData: LiveData<ScreenData>
        get() = _data

    fun markPersistentDataDirty(context: Context){ _persistentData.value = createPersistentData(context) }
    fun updateScreenData(newData: ScreenData){ _data.value = newData }

    private fun createPersistentData(context: Context): ScreenPersistentData {
        return ScreenPersistentData(
            sliderWidth = (PrefValues.sl_width/ context.resources.displayMetrics.densityDpi).dp,
            sliderHeight = (PrefValues.sl_height / context.resources.displayMetrics.densityDpi).dp,
            sliderInitialLocation = (PrefValues.sl_initialLocation/ context.resources.displayMetrics.densityDpi).dp,
            topPadding = PrefValues.sl_TopPadding,
            bottomPadding = PrefValues.sl_DownPadding,
            firstCut = PrefValues.sl_firstCut,
            secondCut = PrefValues.sl_secondCut,
            sliderMovementSpeed = PrefValues.sl_movementSpeed,
        )
    }
}