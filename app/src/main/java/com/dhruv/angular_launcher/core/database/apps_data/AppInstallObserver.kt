package com.dhruv.angular_launcher.core.database.apps_data

import android.content.Context
import android.content.pm.PackageManager
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.dhruv.angular_launcher.core.database.room.dao.AppDataDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppInstallObserver(
    handler: Handler,
    private val packageManager: PackageManager,
    private val appsDao: AppDataDao,
) : ContentObserver(handler) {

    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        CoroutineScope(Dispatchers.IO).launch {
            val newData = AppsDataFunctions.getAllInstalledAppDatas(packageManager)
            newData.forEach { app ->
                appsDao.insert(app)
            }
            appsDao.deleteExtraApps(newData.map { it.packageName })
        }
    }

    companion object {
        fun registerAppInstallObserver(context: Context, packageManager: PackageManager, appsDao: AppDataDao) {
            val contentResolver = context.contentResolver
            val appInstallObserver = AppInstallObserver(Handler(Looper.getMainLooper()), packageManager,appsDao)

            // Define the content URI for the installed packages
            val packagesUri = Uri.parse("content://com.android.packageinstaller")

            // Register the observer to monitor changes to installed packages
            contentResolver.registerContentObserver(
                packagesUri,
                true,
                appInstallObserver
            )
        }
    }
}