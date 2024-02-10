package com.dhruv.angular_launcher.interaction_calculation

import androidx.compose.ui.geometry.Offset
import com.dhruv.angular_launcher.data.models.NavigationMode
import com.dhruv.angular_launcher.data.models.NavigationStage
import com.dhruv.angular_launcher.data.models.SelectionMode

/**
 * c  -> -current-
 * sl -> -slider-
 * ad -> -app drawer-
 */
data class TriggerData(
    var navigationMode: NavigationMode = NavigationMode.NotSelected,
    var navigationStage: NavigationStage = NavigationStage.ModeSelect,
    var selectionMode: SelectionMode = SelectionMode.NotSelected,

    var c_fingerPosition: Offset = Offset.Zero,
    var sl_c_topPositionY: Float = 0f,

    var ad_c_PositionX: Float = 0f,
)
