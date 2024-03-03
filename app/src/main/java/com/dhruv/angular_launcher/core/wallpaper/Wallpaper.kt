package com.dhruv.angular_launcher.core.wallpaper

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.dhruv.angular_launcher.utils.ScreenUtils
import com.dhruv.angular_launcher.utils.toShape

@Composable
fun GlassOverWallpaper (
    modifier: Modifier,
    path: Path,
    blur : Dp = 5.dp,
    shineAlpha : Float = 0.5f,
    edgeWidth : Dp = 2.dp,
){
    GlassOverWallpaper(modifier, path.toShape(), blur, shineAlpha, edgeWidth)
}

@Composable
fun GlassOverWallpaper (
    modifier: Modifier,
    shape: Shape,
    blur : Dp = 5.dp,
    shineAlpha : Float = 0.5f,
    edgeWidth : Dp = 2.dp,
){
    Wallpaper(
        modifier = modifier
            .blur(blur)
            .clip(shape)
    )
    Box(
        modifier = modifier
            .background(
                Brush.linearGradient(
                    0f to Color.Transparent,
                    0.5f to Color.White,
                    1f to Color.Transparent,
                    start = Offset(0f, ScreenUtils.screenHeight),
                    end = Offset(ScreenUtils.screenWidth, 0f),
                )
                ,shape,
                shineAlpha,
            )
            .border(
                width = edgeWidth,
                Color.White,
                shape
            )
    )
}

@Composable
fun Wallpaper (
    modifier: Modifier = Modifier
){
    val wallpaper = WallpaperValues.wallpaper.collectAsState(initial = null).value
    if (wallpaper != null){
        Image(
            bitmap = wallpaper.toBitmap().asImageBitmap(),
            contentDescription = "wallpaper",
            modifier,
            Alignment.Center,
            ContentScale.Crop,
        )
    }
}

fun Modifier.wallpaper( drawable: Drawable? ): Modifier {
    return if (drawable != null)
        this
            .background(
                ShaderBrush(
                    ImageShader(drawable.toBitmap(
                    width = ScreenUtils.screenWidth.toInt(),
                    height = ScreenUtils.screenHeight.toInt()
                ).asImageBitmap())
                )
            )
    else this
}