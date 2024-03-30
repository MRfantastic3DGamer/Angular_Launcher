package com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigationFunctions
import com.dhruv.angular_launcher.core.database.prefferences.values.PrefValues
import com.dhruv.angular_launcher.data.models.IconCoordinatesGenerationScheme

object RadialAppNavigatorValues {
    private val persistentData = MutableLiveData(RadialAppNavigatorPersistentData())
    private val data = MutableLiveData(RadialAppNavigatorData())

    val GetPersistentData: LiveData<RadialAppNavigatorPersistentData>
        get() = persistentData
    val GetData: LiveData<RadialAppNavigatorData>
        get() = data

    fun markPersistentDataDirty (options: List<IconCoordinatesGenerationScheme>){
        val coordinatesPerScale = options.map { RadialAppNavigationFunctions.generateIconCoordinates(it) }

        persistentData.value = RadialAppNavigatorPersistentData(
            offsetsScales = coordinatesPerScale.map { it.iconOffset },
            iconsPerRound = coordinatesPerScale.map { it.iconsPerRound },
            vibration = PrefValues.an_vibration,
        )
    }
    fun updateData (newData: RadialAppNavigatorData){
        data.value = newData
    }
}