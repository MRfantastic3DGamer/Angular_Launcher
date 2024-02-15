package com.dhruv.angular_launcher.accessible_screen.components.slider.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import com.dhruv.angular_launcher.utils.PathToShape

@Composable
fun SliderShape (path: Path){
    Box(
        Modifier
            .fillMaxSize()
            .clip(PathToShape(path))
            .background(Color.Red)
    )
}