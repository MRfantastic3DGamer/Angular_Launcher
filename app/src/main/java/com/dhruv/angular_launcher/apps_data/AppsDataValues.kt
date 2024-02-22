package com.dhruv.angular_launcher.apps_data

import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dhruv.angular_launcher.apps_data.AppsDataFunctions.getAllInstalledAppsData
import com.dhruv.angular_launcher.apps_data.model.AppsData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object AppsDataValues {
    private enum class UpdateState { UPDATING, IDLE }

    private val data = MutableLiveData<AppsData?>(null)
    private val prevData = MutableLiveData<AppsData?>(null)
    private var updateState = UpdateState.IDLE

    val getData: LiveData<AppsData?>
        get() = when(updateState) {
            UpdateState.UPDATING -> prevData
            UpdateState.IDLE -> data
        }

    fun initialize(packageManager: PackageManager) {
        updateState = UpdateState.UPDATING

        CoroutineScope(Dispatchers.IO).launch {
            val newData = async { getAllInstalledAppsData(packageManager) }

            prevData.postValue(data.value)

            data.postValue(newData.await())

            updateState = UpdateState.IDLE
        }
    }
}