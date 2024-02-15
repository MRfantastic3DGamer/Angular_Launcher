package com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data.RadialAppNavigatorValues

class RadialAppNavigatorVM:ViewModel() {
    var center: Offset by mutableStateOf(Offset(500f, 100f))
    var numberOfElements: Int by mutableStateOf(10)
    var offsets: List<Offset> by mutableStateOf(listOf())

    init {
        RadialAppNavigatorValues.GetData.observeForever {
            center = it.center
            numberOfElements = RadialAppNavigatorValues.GetPersistentData.value?.numberOfElementsPerSelection?.get(it.currentSelectionIndex) ?: 0
            offsets = RadialAppNavigationFunctions.getUsableOffsets(
                RadialAppNavigatorValues.GetPersistentData.value?.offsetsScales ?: listOf(),
                center,
                numberOfElements
            )
        }
    }
}