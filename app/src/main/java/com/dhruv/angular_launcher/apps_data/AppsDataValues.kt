package com.dhruv.angular_launcher.apps_data

import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dhruv.angular_launcher.apps_data.AppsDataFunctions.getAllAppIcons
import com.dhruv.angular_launcher.apps_data.AppsDataFunctions.getAllInstalledAppDatas
import com.dhruv.angular_launcher.apps_data.model.AppData
import com.example.launcher.Drawing.DrawablePainter
import io.realm.kotlin.UpdatePolicy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object AppsDataValues {
    enum class UpdateState { UPDATING, IDLE }

    private val appsData = MutableLiveData<List<AppData>?>(null)
    private val prevAppsData = MutableLiveData<List<AppData>?>(null)
    private var _appUpdateState = MutableLiveData(UpdateState.IDLE)
    val appState: LiveData<UpdateState>
        get() = _appUpdateState
    val getAppsData: LiveData<List<AppData>?>
        get() = when(_appUpdateState.value!!) {
            UpdateState.UPDATING -> prevAppsData
            UpdateState.IDLE -> appsData
        }

    private val appsIconData = MutableLiveData<Map<String,DrawablePainter>?>(null)
    private val prevAppsIconData = MutableLiveData<Map<String,DrawablePainter>?>(null)
    private var _appIconsUpdateState = MutableLiveData(UpdateState.IDLE)
    val appIconsState: LiveData<UpdateState>
        get() = _appIconsUpdateState
    val getAppsIcons: LiveData<Map<String,DrawablePainter>?>
        get() = when(_appIconsUpdateState.value!!) {
            UpdateState.UPDATING -> prevAppsIconData
            UpdateState.IDLE -> appsIconData
        }

    fun initialize(packageManager: PackageManager) {
        _appUpdateState.value = UpdateState.UPDATING
        val realm = MyApplication.realm

        CoroutineScope(Dispatchers.IO).launch {
            val newData = async { getAllInstalledAppDatas(packageManager) }

            prevAppsData.postValue(appsData.value)

            appsData.postValue(newData.await())

            newData.await().forEach { app ->
                realm.write {
                    copyToRealm(app, UpdatePolicy.ALL)
                }
            }

            _appUpdateState.postValue(UpdateState.IDLE)
        }


        _appIconsUpdateState.value = UpdateState.UPDATING
        CoroutineScope(Dispatchers.IO).launch {
            val newData = async { getAllAppIcons(packageManager) }

            prevAppsIconData.postValue(appsIconData.value)

            appsIconData.postValue(newData.await())

            _appIconsUpdateState.postValue(UpdateState.IDLE)
        }
    }
}