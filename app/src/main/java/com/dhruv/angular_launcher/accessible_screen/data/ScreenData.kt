package com.dhruv.angular_launcher.accessible_screen.data

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.data.models.NavigationMode
import com.dhruv.angular_launcher.data.models.NavigationStage
import com.dhruv.angular_launcher.data.models.SelectionMode

@Stable
data class ScreenData(
    val touchPosition: Offset = Offset.Zero,
    val delta: Offset? = null,
)
