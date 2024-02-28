package com.dhruv.angular_launcher.utils.wallpaper

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.view.WindowManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.ViewModel

class WallpaperFullScreenViewModel : ViewModel() {
    var scale by mutableStateOf(ContentScale.Crop)
    var showFitScreenBtn by mutableStateOf(true)
    var showCropScreenBtn by mutableStateOf(false)
    var id by mutableStateOf(0)
    var setOriginalWallpaperDialog = mutableStateOf(false)
    var setWallpaperAs by mutableStateOf(1)
    var interstitialState = mutableStateOf(0)
    var saturationSliderValue = mutableStateOf(1f)
    var saturationSliderPosition = mutableStateOf(1f)
    var scaleFitState by mutableStateOf(true)
    var scaleCropState by  mutableStateOf(false)
    var scaleStretchState by  mutableStateOf(false)
    var finalScaleState by  mutableStateOf(scaleFitState)

//@RequiresApi(Build.VERSION_CODES.N)
    /**
     * @param wallpaperAs-> 1-home, 2-lock, 3-both
     */
    fun setWallPaper(
        context: Context,
        wallpaperAs: Int,
        finalImageBitmap: Bitmap?,
    ) {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val screenWidth = windowManager.defaultDisplay.width
        val screenHeight = windowManager.defaultDisplay.height
        val wallpaperManager = WallpaperManager.getInstance(context)
        wallpaperManager.suggestDesiredDimensions(screenWidth, screenHeight)
        val width = wallpaperManager.desiredMinimumWidth
        val height = wallpaperManager.desiredMinimumHeight

        val thread = Thread {
            try {
                when (finalScaleState) {
                    scaleCropState -> {
                        val wallpaper = finalImageBitmap?.let {
                            scaleAndCropBitmap(it, screenWidth, screenHeight)
                        }
                        when (wallpaperAs) {
                            1 -> wallpaperManager.setBitmap(
                                wallpaper,
                                null,
                                true,
                                WallpaperManager.FLAG_SYSTEM
                            )
                            2 -> wallpaperManager.setBitmap(
                                wallpaper,
                                null,
                                true,
                                WallpaperManager.FLAG_LOCK
                            )
                            3 -> {
                                wallpaperManager.setBitmap(
                                    wallpaper,
                                    null,
                                    true,
                                    WallpaperManager.FLAG_LOCK
                                )
                                wallpaperManager.setBitmap(
                                    wallpaper,
                                    null,
                                    true,
                                    WallpaperManager.FLAG_SYSTEM
                                )
                            }
                        }
                    }
                    scaleStretchState -> {
                        val wallpaper =
                            finalImageBitmap?.let { Bitmap.createScaledBitmap(it, width, height, true) }
                        when (wallpaperAs) {
                            1 -> wallpaperManager.setBitmap(
                                wallpaper,
                                null,
                                true,
                                WallpaperManager.FLAG_SYSTEM
                            )
                            2 -> wallpaperManager.setBitmap(
                                wallpaper,
                                null,
                                true,
                                WallpaperManager.FLAG_LOCK
                            )
                            3 -> {
                                wallpaperManager.setBitmap(
                                    wallpaper,
                                    null,
                                    true,
                                    WallpaperManager.FLAG_LOCK
                                )
                                wallpaperManager.setBitmap(
                                    wallpaper,
                                    null,
                                    true,
                                    WallpaperManager.FLAG_SYSTEM
                                )
                            }
                        }
                    }
                    else -> {
                        val wallpaper = finalImageBitmap?.let {
                            scaleAndFitBitmap(it, screenWidth, screenHeight)
                        }
                        when (wallpaperAs) {
                            1 -> wallpaperManager.setBitmap(
                                wallpaper,
                                null,
                                true,
                                WallpaperManager.FLAG_SYSTEM
                            )
                            2 -> wallpaperManager.setBitmap(
                                wallpaper,
                                null,
                                true,
                                WallpaperManager.FLAG_LOCK
                            )
                            3 -> {
                                wallpaperManager.setBitmap(
                                    wallpaper,
                                    null,
                                    true,
                                    WallpaperManager.FLAG_LOCK
                                )
                                wallpaperManager.setBitmap(
                                    wallpaper,
                                    null,
                                    true,
                                    WallpaperManager.FLAG_SYSTEM
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        thread.start()
    }

    private fun scaleAndFitBitmap(bitmap: Bitmap, targetWidth: Int, targetHeight: Int): Bitmap {
        val sourceWidth = bitmap.width
        val sourceHeight = bitmap.height

        val widthRatio = targetWidth.toFloat() / sourceWidth.toFloat()
        val heightRatio = targetHeight.toFloat() / sourceHeight.toFloat()

        val scaleFactor = widthRatio.coerceAtMost(heightRatio)

        val scaledWidth = (sourceWidth * scaleFactor).toInt()
        val scaledHeight = (sourceHeight * scaleFactor).toInt()

        val offsetX = (targetWidth - scaledWidth) / 2
        val offsetY = (targetHeight - scaledHeight) / 2

        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true)

        val outputBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(outputBitmap)
        canvas.drawBitmap(scaledBitmap, offsetX.toFloat(), offsetY.toFloat(), null)

        return outputBitmap
    }

    private fun scaleAndCropBitmap(bitmap: Bitmap, targetWidth: Int, targetHeight: Int): Bitmap {
        val sourceWidth = bitmap.width
        val sourceHeight = bitmap.height

        val widthRatio = targetWidth.toFloat() / sourceWidth.toFloat()
        val heightRatio = targetHeight.toFloat() / sourceHeight.toFloat()

        val scaleFactor = widthRatio.coerceAtLeast(heightRatio)

        val scaledWidth = (sourceWidth * scaleFactor).toInt()
        val scaledHeight = (sourceHeight * scaleFactor).toInt()

        val offsetX = (targetWidth - scaledWidth) / 2
        val offsetY = (targetHeight - scaledHeight) / 2

        val scaledBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(scaledBitmap)
        val scaleMatrix = RectF(
            offsetX.toFloat(),
            offsetY.toFloat(),
            (offsetX + scaledWidth).toFloat(),
            (offsetY + scaledHeight).toFloat()
        )
        canvas.drawBitmap(bitmap, null, scaleMatrix, null)

        return scaledBitmap
    }

    private fun centerCropBitmap(bitmap: Bitmap, targetWidth: Int, targetHeight: Int): Bitmap {
        val sourceWidth = bitmap.width
        val sourceHeight = bitmap.height

        val scale = if (sourceWidth > sourceHeight) {
            targetWidth.toFloat() / sourceWidth
        } else {
            targetHeight.toFloat() / sourceHeight
        }

        val scaledWidth = (scale * sourceWidth).toInt()
        val scaledHeight = (scale * sourceHeight).toInt()

        val startX = (targetWidth - scaledWidth) / 2
        val startY = (targetHeight - scaledHeight) / 2

        return Bitmap.createScaledBitmap(
            bitmap,
            scaledWidth,
            scaledHeight,
            true
        ).let {
            Bitmap.createBitmap(it, startX, startY, targetWidth, targetHeight)
        }
    }
}