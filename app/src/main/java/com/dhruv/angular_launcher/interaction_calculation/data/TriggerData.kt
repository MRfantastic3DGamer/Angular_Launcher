package com.dhruv.angular_launcher.interaction_calculation.data

import androidx.compose.ui.geometry.Offset
import com.dhruv.angular_launcher.data.enums.NavigationMode
import com.dhruv.angular_launcher.data.enums.NavigationStage
import com.dhruv.angular_launcher.data.enums.SelectionMode

/**
 * s  -> -screen-
 * c  -> -current-
 * sl -> -slider-
 * ad -> -app drawer-
 */
data class TriggerData(

    var navigationMode: NavigationMode = NavigationMode.NotSelected,
    var navigationStage: NavigationStage = NavigationStage.SelectionModeSelection,
    var selectionMode: SelectionMode = SelectionMode.NotSelected,

    var sl_height: Float = 1000f,
    var sl_width: Float = 500f,

    var c_fingerPosition: Offset = Offset.Zero,
    var sl_c_topPositionY: Float = 0f,

    var ad_c_PositionX: Float = 0f,
)
