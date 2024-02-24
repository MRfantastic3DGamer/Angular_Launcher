package com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhruv.angular_launcher.accessible_screen.components.app_label.data.AppLabelValue
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigationFunctions.getFirstChar
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data.RadialAppNavigatorValues
import com.dhruv.angular_launcher.accessible_screen.data.VibrationData
import com.dhruv.angular_launcher.apps_data.MyApplication
import com.dhruv.angular_launcher.apps_data.model.AppData
import com.dhruv.angular_launcher.apps_data.model.GroupData
import com.dhruv.angular_launcher.data.models.SelectionMode
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class RadialAppNavigatorVM:ViewModel() {



    private val realm = MyApplication.realm
    val appsByChar: StateFlow<Map<String, List<String>>> = realm
        .query<AppData>()
        .asFlow()
        .map { res ->
            val packagesPerChar = mutableMapOf<String, MutableList<String> >()
            res.list.forEach {  app ->
                val firstChar = getFirstChar(app.name)
                if (firstChar in packagesPerChar)
                    packagesPerChar[firstChar]!!.add(app.packageName)
                else
                    packagesPerChar[firstChar] = mutableListOf(app.packageName)
            }
            packagesPerChar.toMap()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyMap())
    val groups = realm
        .query<GroupData>()
        .asFlow()
        .map { res -> res.list.toList() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    var visibility by mutableStateOf(false)
    var selectionMode: SelectionMode by mutableStateOf(SelectionMode.NotSelected)

    var iconSize by mutableStateOf(0f)
    var enlargeSelectedIconBy by mutableStateOf(0f)
    var shouldBlur by mutableStateOf(false)
    var blurAmount by mutableStateOf(0f)
    var tint by mutableStateOf(Color.Black)

    // feel
    var vibration by mutableStateOf(VibrationData())

    var roundsStartingDistances: List<List<Float>> by mutableStateOf(listOf())
    var iconsPerRound: List<List<Int>> by mutableStateOf(listOf())


    var sliderPosY: Float by mutableStateOf(0f)
    var center: Offset by mutableStateOf(Offset(500f, 100f))
    var offsetFromCenter: Offset by mutableStateOf(Offset.Zero)

    var possibleSelections: List<Int> by mutableStateOf(listOf())
    var sliderSelection: String by mutableStateOf("@")

    var selectionIndex: Int by mutableIntStateOf(-1)

    var shouldSelectApp: Boolean by mutableStateOf(false)

    init {
        RadialAppNavigatorValues.GetPersistentData.observeForever {

            iconSize = it.iconSize
            enlargeSelectedIconBy = it.enlargeSelectedIconBy
            shouldBlur = it.shouldBlur
            blurAmount = it.blurAmount
            tint = it.tint

            // feel
            vibration = it.vibration

            roundsStartingDistances = it.roundStartingDistances
            iconsPerRound = it.iconsPerRound
        }
        RadialAppNavigatorValues.GetData.observeForever {
            sliderSelection = it.sliderSelection
            visibility = it.visibility
            center = it.center
            sliderPosY = it.sliderPositionY
            selectionMode = it.selectionMode

            offsetFromCenter = it.offsetFromCenter

            shouldSelectApp = it.shouldSelectApp
        }
    }
}