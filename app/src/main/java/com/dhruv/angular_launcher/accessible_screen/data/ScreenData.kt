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
    val delta: Offset = Offset.Zero,
    val navigationMode: NavigationMode = NavigationMode.NotSelected,
    val selectionMode: SelectionMode = SelectionMode.NotSelected,
    val navigationStage: NavigationStage = NavigationStage.Inactive,
)
