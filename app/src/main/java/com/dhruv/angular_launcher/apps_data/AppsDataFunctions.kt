package com.dhruv.angular_launcher.apps_data

import android.content.Intent
import android.content.pm.PackageManager
import com.dhruv.angular_launcher.apps_data.model.AppsData
import com.example.launcher.Drawing.DrawablePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AppsDataFunctions {
    suspend fun getAllInstalledAppsData (packageManager: PackageManager): AppsData {
        return withContext(Dispatchers.IO) {
            val main = Intent(Intent.ACTION_MAIN, null)
            main.addCategory(Intent.CATEGORY_LAUNCHER)
            val appsL = packageManager.queryIntentActivities(main, 0)

            val names: MutableList<String?> = mutableListOf()
            val packages: MutableList<String?> = mutableListOf()
            val icons: MutableList<DrawablePainter?> = mutableListOf()

            for (app in appsL) {
                names.add(app.loadLabel(packageManager) as String)
                packages.add(app.activityInfo.packageName)
                // GetIntentByPackageName packageManager.getLaunchIntentForPackage(app.activityInfo.packageName)
                icons.add(DrawablePainter(app.loadIcon(packageManager)))
            }

            AppsData(names, packages, icons)
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