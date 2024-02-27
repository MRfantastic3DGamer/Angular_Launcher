package com.dhruv.angular_launcher.database.apps_data

import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dhruv.angular_launcher.database.apps_data.AppsDataFunctions.getAllAppIcons
import com.dhruv.angular_launcher.database.apps_data.AppsDataFunctions.getAllInstalledAppDatas
import com.dhruv.angular_launcher.database.room.dao.AppDataDao
import com.example.launcher.Drawing.DrawablePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object AppsIconsDataValues {

    private val appsIconData = MutableLiveData<Map<String,DrawablePainter>?>(null)
    val getAppsIcons: LiveData<Map<String,DrawablePainter>?>
        get() = appsIconData

    fun initialize(packageManager: PackageManager, appsDao: AppDataDao) {

        CoroutineScope(Dispatchers.IO).launch {
            val newData = async { getAllAppIcons(packageManager) }
            appsIconData.postValue(newData.await())
        }

        CoroutineScope(Dispatchers.IO).launch {
            val newData = getAllInstalledAppDatas(packageManager)
            newData.forEach { app ->
                appsDao.insert(app)
            }
        }
    }
}