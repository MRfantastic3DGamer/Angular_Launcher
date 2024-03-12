package com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data

import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigationFunctions
import com.dhruv.angular_launcher.accessible_screen.data.AccessibleScreenValues
import com.dhruv.angular_launcher.core.database.prefferences.values.PrefValues
import com.dhruv.angular_launcher.utils.ScreenUtils
import com.dhruv.angular_launcher.core.appIcon.IconStyle

object RadialAppNavigatorValues {
    private val persistentData = MutableLiveData(RadialAppNavigatorPersistentData())
    private val data = MutableLiveData(RadialAppNavigatorData())

    val GetPersistentData: LiveData<RadialAppNavigatorPersistentData>
        get() = persistentData
    val GetData: LiveData<RadialAppNavigatorData>
        get() = data

    fun markPersistentDataDirty (){
        val inputs = listOf(PrefValues.an_option1, PrefValues.an_option2, PrefValues.an_option3, PrefValues.an_option4, PrefValues.an_option5)
        val coordinatesPerScale = inputs.map { RadialAppNavigationFunctions.generateIconCoordinates(it) }

        persistentData.value = RadialAppNavigatorPersistentData(
            numberOfElementsPerSelection = GetElementsPerSelection(),
            width = ScreenUtils.screenWidth - ScreenUtils.dpToF(AccessibleScreenValues.GetPersistentData.value?.sliderWidth ?: 0.dp),
            offsetsScales = coordinatesPerScale.map { it.iconOffset },
            iconsPerRound = coordinatesPerScale.map { it.iconsPerRound },
            roundStartingDistances = coordinatesPerScale.map{ it.startingPointOfRound },
            tint = PrefValues.an_tint,
            vibration = PrefValues.an_vibration,
            shouldBlur = PrefValues.an_shouldBlur,
            blurAmount = PrefValues.an_blurAmount,
            iconStyle = PrefValues.an_iconStyle,
            selectedIconStyle = PrefValues.an_selectedIconStyle,
        )
    }
    fun updateData (newData: RadialAppNavigatorData){
        data.value = newData
    }

    fun GetElementsPerSelection(): List<Int> { return listOf(50,10,34,8,20,35,25,20,19,70,5,10,24,4,8,20,35,25,20,19,70,15,10,4,4,8,20,35,25,20,19,70) }
}