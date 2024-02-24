package com.dhruv.angular_launcher.apps_data

import android.content.Intent
import android.content.pm.PackageManager
import com.dhruv.angular_launcher.apps_data.model.AppData
import com.dhruv.angular_launcher.apps_data.model.AppsData
import com.example.launcher.Drawing.DrawablePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AppsDataFunctions {
    suspend fun getAllInstalledAppDatas (packageManager: PackageManager): List<AppData> {
        return withContext(Dispatchers.IO) {
            val main = Intent(Intent.ACTION_MAIN, null)
            main.addCategory(Intent.CATEGORY_LAUNCHER)
            val appsL = packageManager.queryIntentActivities(main, 0)

            val apps: MutableList<AppData> = mutableListOf()

            val icons: MutableList<DrawablePainter?> = mutableListOf()

            for (app in appsL) {
                apps.add(AppData().apply {
                    this.name = app.loadLabel(packageManager) as String
                    this.packageName = app.activityInfo.packageName
                })
                icons.add(DrawablePainter(app.loadIcon(packageManager)))
            }

            apps.toList()
        }
    }

    suspend fun getAllAppIcons (packageManager: PackageManager): Map<String, DrawablePainter>{
        return withContext(Dispatchers.IO) {
            val main = Intent(Intent.ACTION_MAIN, null)
            main.addCategory(Intent.CATEGORY_LAUNCHER)
            val appsL = packageManager.queryIntentActivities(main, 0)

            val icons: MutableMap<String, DrawablePainter> = mutableMapOf()

            for (app in appsL) {
                icons[app.activityInfo.packageName] = DrawablePainter(app.loadIcon(packageManager))
            }

            icons
        }
    }

    fun getUsableApps (data: AppsData): AppsData {

        val names: MutableList<String?> = mutableListOf()
        val packages: MutableList<String?> = mutableListOf()
        val icons: MutableList<DrawablePainter?> = mutableListOf()

        data.packages.indices.forEach { i ->
            if (data.icons[i] == null || data.names[i] == null || data.packages[i] == null){

            }
            else{
                names.add(data.names[i])
                packages.add(data.packages[i])
                icons.add(data.icons[i])
            }
        }
        return AppsData( names, packages, icons )
    }
}