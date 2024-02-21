package com.dhruv.angular_launcher.settings_module.prefferences.values

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorLooks
import com.dhruv.angular_launcher.accessible_screen.data.AccessibleScreenValues
import com.dhruv.angular_launcher.settings_module.prefferences.PreferencesManager

/**
 * sl -> -slider- on right side of screen
 * t  -> -theme-
 * fc -> -fluid cursor-
 * an -> -app navigation-
 */
object PrefValues {



    // region fluid cursor
    val fc_animationSpeed: Float = 1f
    val fc_fluidCursorLooks: FluidCursorLooks = FluidCursorLooks()
    // endregion

    // region slider

    val sl_vibrationTime: Float = 1f
    val sl_vibrationAmount: Float = 1f
    val sl_vibrateOnSelectionChange: Boolean = false
    val sl_tint: Color = Color.Black
    val sl_shouldBlur: Boolean = false
    val sl_selectionRadios: Float = 100f
    val sl_sidePadding: Float = 100f
    val sl_blurAmount: Float = 1f
    val sl_animationSpeed: Float = 1f
    var sl_width: Float = 100f
    var sl_height: Float = 1300f
    var sl_topPadding: Float = 50f
    var sl_downPadding: Float = 50f
    var sl_movementSpeed: Float = 1f
    var sl_triggerCurveEdgeCount: Int = 15
    var sl_selectionCurveOffset: Float = 50f
    // endregion

    var s_firstCut: Float = 0.333f
    var s_secondCut: Float = 0.666f


    val changedValuesMap = mutableMapOf<String, Any>()

    fun loadAllValues(context: Context){
        val prefManager = PreferencesManager.getInstance(context)

        sl_width = prefManager.getData("sl_width", sl_width)
        sl_height = prefManager.getData("sl_height", sl_height)
        sl_topPadding = prefManager.getData("sl_TopPadding", sl_topPadding)
        sl_downPadding = prefManager.getData("sl_DownPadding", sl_downPadding)
        sl_movementSpeed = prefManager.getData("sl_movementSpeed", sl_movementSpeed)
        sl_triggerCurveEdgeCount = prefManager.getData("sl_TriggerCurveEdgeCount", sl_triggerCurveEdgeCount)
        sl_selectionCurveOffset = prefManager.getData("sl_selectionCurveOffset", sl_selectionCurveOffset)
        s_firstCut = prefManager.getData("s_firstCut", s_firstCut)
        s_secondCut = prefManager.getData("s_secondCut", s_secondCut)

        AccessibleScreenValues.markPersistentDataDirty()
    }

    fun save(context: Context){
        val prefManager = PreferencesManager.getInstance(context)
        println("saving")
        changedValuesMap.forEach { (key, value) ->
            println("$key -> $value")
            prefManager.saveData(key, value.toString())
        }
        println("saving complete")
        loadAllValues(context)
    }
}