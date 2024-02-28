package com.dhruv.angular_launcher.accessible_screen.components.slider.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.core.wallpaper.GlassOverWallpaper

@Composable
fun SliderShape (path: Path){
    GlassOverWallpaper(
        Modifier.fillMaxSize(),
        path = path,
        blur = 10.dp
    )
}