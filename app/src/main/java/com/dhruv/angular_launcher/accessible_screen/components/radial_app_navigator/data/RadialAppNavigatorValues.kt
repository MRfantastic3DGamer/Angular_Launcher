package com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigationFunctions
import com.dhruv.angular_launcher.accessible_screen.data.AccessibleScreenValues
import com.dhruv.angular_launcher.utils.ScreenUtils

object RadialAppNavigatorValues {
    private val persistentData = MutableLiveData(RadialAppNavigatorPersistentData())
    private val data = MutableLiveData(RadialAppNavigatorData(sliderPositionY = 0f, offsetFromCenter = Offset.Zero))

    val GetPersistentData: LiveData<RadialAppNavigatorPersistentData>
        get() = persistentData
    val GetData: LiveData<RadialAppNavigatorData>
        get() = data

    fun markPersistentDataDirty (){
        val inputs = listOf(
            RadialAppNavigationFunctions.IconCoordinatesGenerationInput(
                250.0,150.0,150.0
            ),
            RadialAppNavigationFunctions.IconCoordinatesGenerationInput(
                250.0,125.0,125.0
            ),
            RadialAppNavigationFunctions.IconCoordinatesGenerationInput(
                250.0,100.0,100.0
            ),
            RadialAppNavigationFunctions.IconCoordinatesGenerationInput(
                250.0,750.0,75.0, 30
            ),
            RadialAppNavigationFunctions.IconCoordinatesGenerationInput(
                250.0,50.0,50.0, 40
            ),
        )
        val coordinatesPerScale = inputs.map { RadialAppNavigationFunctions.generateIconCoordinates(it) }

        persistentData.value = RadialAppNavigatorPersistentData(
            numberOfElementsPerSelection = GetElementsPerSelection(),
            width = ScreenUtils.screenWidth - ScreenUtils.dpToF(AccessibleScreenValues.GetPersistentData.value?.sliderWidth ?: 0.dp),
            offsetsScales = coordinatesPerScale.map { it.iconOffset },
            iconsPerRound = coordinatesPerScale.map { it.iconsPerRound },
            roundStartingDistances = coordinatesPerScale.map{ it.startingPointOfRound },
        )
    }
    fun updateData (newData: RadialAppNavigatorData){
        data.value = newData
    }

    fun GetElementsPerSelection(): List<Int> { return listOf(50,10,34,8,20,35,25,20,19,70,5,10,24,4,8,20,35,25,20,19,70,15,10,4,4,8,20,35,25,20,19,70) }
}