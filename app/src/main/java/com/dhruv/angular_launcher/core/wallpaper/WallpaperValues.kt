package com.dhruv.angular_launcher.core.wallpaper

import android.app.WallpaperManager
import android.content.Context
import android.graphics.drawable.Drawable
import kotlinx.coroutines.flow.flow

object WallpaperValues {
    private var wallpaperDrawable: Drawable? = null

    val wallpaper = flow { emit(wallpaperDrawable) }

    fun getWallpaper (context: Context) {
        val wallpaperManager = WallpaperManager.getInstance(context)
        wallpaperDrawable = wallpaperManager.drawable
    }
}