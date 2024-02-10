package com.dhruv.angular_launcher.accessible_screen.data

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.data.models.NavigationMode
import com.dhruv.angular_launcher.data.models.NavigationStage
import com.dhruv.angular_launcher.data.models.SelectionMode

data class ScreenData(
    var touchLocation: Float = 0f,
    var sliderTopPosition: Float = 0f,
    var navigationMode: NavigationMode = NavigationMode.NotSelected,
    var selectionMode: SelectionMode = SelectionMode.NotSelected,
    var navigationStage: NavigationStage = NavigationStage.Inactive,
)
