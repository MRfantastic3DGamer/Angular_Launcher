package com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dhruv.angular_launcher.core.database.prefferences.values.PrefValues

object FluidCursorValues {
    private var persistentData = MutableLiveData(FluidCursorPersistentData(FluidCursorLooks(), 1f))
    private var data = MutableLiveData(FluidCursorData())

    val GetPersistentData: LiveData<FluidCursorPersistentData>
        get() = persistentData
    val GetData: LiveData<FluidCursorData>
        get() = data

    fun markPersistentDataDirty (){
        persistentData.value = FluidCursorPersistentData(
            fluidCursorLooks = PrefValues.fc_fluidCursorLooks,
            animationSpeed = PrefValues.fc_animationSpeed,

        )
    }
    fun updateData (newData: FluidCursorData){
        data.value = newData
    }
}