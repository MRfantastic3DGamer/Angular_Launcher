package com.dhruv.angular_launcher.settings_module.prefferences.values

import android.content.Context
import com.dhruv.angular_launcher.accessible_screen.data.ScreenValues
import com.dhruv.angular_launcher.settings_module.prefferences.PreferencesManager

/**
 * sl -> -slider- on right side of screen
 * t  -> -theme-
 */
object PrefValues {

    var sl_width: Float = 100f
    var sl_height: Float = 1300f
    var sl_initialLocation: Float = 0f
    var sl_TopPadding: Float = 0f
    var sl_DownPadding: Float = 0f
    var sl_firstCut: Float = 0.333f
    var sl_secondCut: Float = 0.666f
    var sl_movementSpeed: Float = 1f
    var sl_TriggerCurveEdgeCount: Int = 15
    var sl_selectionCurveOffset: Float = 50f

    fun getAllValues(context: Context){
        val prefManager = PreferencesManager.getInstance(context)

        sl_width = prefManager.getData("sl_width", sl_width)
        sl_height = prefManager.getData("sl_height", sl_height)
        sl_initialLocation = prefManager.getData("sl_initialLocation", sl_initialLocation)
        sl_TopPadding = prefManager.getData("sl_TopPadding", sl_TopPadding)
        sl_DownPadding = prefManager.getData("sl_DownPadding", sl_DownPadding)
        sl_firstCut = prefManager.getData("sl_firstCut", sl_firstCut)
        sl_secondCut = prefManager.getData("sl_secondCut", sl_secondCut)
        sl_movementSpeed = prefManager.getData("sl_movementSpeed", sl_movementSpeed)
        sl_TriggerCurveEdgeCount = prefManager.getData("sl_TriggerCurveEdgeCount", sl_TriggerCurveEdgeCount)
        sl_selectionCurveOffset = prefManager.getData("sl_selectionCurveOffset", sl_selectionCurveOffset)

        ScreenValues.markPersistentDataDirty()
    }

    val changedValuesMap = mutableMapOf<String, Any>()
    fun save(context: Context){
        val prefManager = PreferencesManager.getInstance(context)
        println("saving")
        changedValuesMap.forEach { (key, value) ->
            println("$key -> $value")
            prefManager.saveData(key, value.toString())
        }
        println("saving complete")
        getAllValues(context)
    }
}