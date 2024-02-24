package com.dhruv.angular_launcher.accessible_screen.components.slider

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhruv.angular_launcher.accessible_screen.components.app_label.AppLabelFunctions
import com.dhruv.angular_launcher.accessible_screen.components.app_label.data.AppLabelValue
import com.dhruv.angular_launcher.accessible_screen.components.slider.SliderFunctions.GetSliderPositionY
import com.dhruv.angular_launcher.accessible_screen.components.slider.data.SliderValues
import com.dhruv.angular_launcher.apps_data.MyApplication
import com.dhruv.angular_launcher.apps_data.model.AppData
import com.dhruv.angular_launcher.apps_data.model.GroupData
import com.dhruv.angular_launcher.data.enums.SelectionMode
import com.dhruv.angular_launcher.utils.ScreenUtils
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SliderVM:ViewModel() {

    private val realm = MyApplication.realm

    var selectionMode: SelectionMode by mutableStateOf(SelectionMode.NotSelected)
    val alphabets = realm
        .query<AppData>()
        .asFlow()
        .map { res -> res.list.toList().map {
            val f = it.name.first()
            if (f.isLetter()) f.uppercase()
            else "#"
            }.sorted().toSet()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val groups = realm
        .query<GroupData>()
        .asFlow()
        .map { res -> res.list.toList().map { it.iconKey } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val elementsCount: Int
        get() = when(selectionMode) {
            SelectionMode.NotSelected -> 0
            SelectionMode.ByAlphabet -> alphabets.value.size
            SelectionMode.BySearch -> 0
            SelectionMode.ByGroup -> groups.value.size
        }
    val allOptions: List<String>
        get() = when(selectionMode) {
            SelectionMode.NotSelected -> emptyList()
            SelectionMode.ByAlphabet -> alphabets.value.toList()
            SelectionMode.BySearch -> emptyList()
            SelectionMode.ByGroup -> groups.value
        }

    // main
    var height: Float by mutableStateOf(500f)
    var width: Float by mutableStateOf(100f)

    // constraints
    var TopPadding by mutableStateOf(50f)
    var DownPadding by mutableStateOf(50f)

    // looks
    var TriggerCurveEdgeCount by mutableStateOf(15)
    var selectionCurveOffset by mutableStateOf(50f)
    var shouldBlur by mutableStateOf(false)
    var blurAmount by mutableStateOf(1f)
    var tint by mutableStateOf(Color.Black)

    // feel
    var vibrateOnSelectionChange by mutableStateOf(false)
    var vibrationAmount by mutableStateOf(1f)
    var vibrationTime by mutableStateOf(0f)

    var sidePadding: Float by mutableStateOf(0f)

    var visible: Boolean by mutableStateOf(false)
    var sliderPos: Offset by mutableStateOf(Offset.Zero)
    var selectionIndex: Int by mutableStateOf(0)
    var selectionPosY: Float by mutableStateOf(0f)
    var touchPos: Offset by mutableStateOf(Offset.Zero)

    init {
        SliderValues.GetPersistentData.observeForever {
            TopPadding = it.topPadding
            DownPadding = it.DownPadding
            TriggerCurveEdgeCount = it.triggerCurveEdgeCount
            selectionCurveOffset = it.selectionCurveOffset
            shouldBlur = it.shouldBlur
            blurAmount = it.blurAmount
            tint = it.tint
            vibrateOnSelectionChange = it.vibrateOnSelectionChange
            vibrationAmount = it.vibrationAmount
            vibrationTime = it.vibrationTime

            height = ScreenUtils.dpToF(it.height)
            sidePadding = it.sidePadding
        }
        SliderValues.GetSliderData.observeForever {
            touchPos = it.touchPos
            selectionMode = it.selectionMode
            if (it.shouldUpdateOffset){
                val posY = GetSliderPositionY(touchPos.y, height, sliderPos.y)
                sliderPos = SliderFunctions.calculateSliderPosition(posY)

                val appLabelPosition = AppLabelFunctions.calculatePosition(300f, sliderPos.y, height, 20f)
                AppLabelValue.updatePos(appLabelPosition)
            }

            if (it.shouldUpdateSelection) {
                val selection = SliderFunctions.calculateCurrentSelection(
                    elementsCount-1,
                    height,
                    touchPos.y - sliderPos.y
                )
                selectionIndex = selection.index
                selectionPosY = selection.posY
            visible = (selectionIndex != -1) && it.visible
            }
        }
    }
}
