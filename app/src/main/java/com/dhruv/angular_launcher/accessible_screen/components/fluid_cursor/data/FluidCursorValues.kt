package com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object FluidCursorValues {
    private var persistentData = MutableLiveData(FluidCursorPersistentData())
    private var data = MutableLiveData(FluidCursorData())

    val GetPersistentData: LiveData<FluidCursorPersistentData>
        get() = persistentData
    val GetData: LiveData<FluidCursorData>
        get() = data

    fun markPersistentDataDirty (){
        persistentData.value = FluidCursorPersistentData()
    }
    fun updateData (newData: FluidCursorData){
        data.value = newData
    }
}