package com.dhruv.angular_launcher.core.database.prefferences.values

import android.content.Context
import com.dhruv.angular_launcher.accessible_screen.data.AccessibleScreenValues
import com.dhruv.angular_launcher.accessible_screen.data.VibrationData
import com.dhruv.angular_launcher.core.database.prefferences.PreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * sl -> -slider- on right side of screen
 * t  -> -theme-
 * fc -> -fluid cursor-
 * an -> -app navigation-
 */
object PrefValues {

    // region theme

    var theme_id: Int = 0

    // endregion

    // region app navigation
    var an_vibration: VibrationData = VibrationData(true, 1f, 1f)
    // endregion

    // region fluid cursor
    // endregion

    // region slider

    var sl_vibration: VibrationData = VibrationData(active = true)
    // endregion

    var s_firstCut: Float = 0.333f
    var s_secondCut: Float = 0.666f


    val changedValuesMap = mutableMapOf<String, Any>()

    fun loadAllValues(context: Context){
        val prefManager = PreferencesManager.getInstance(context)

// region theme
        theme_id = prefManager.getData("theme_id", theme_id)
// endregion

// region app navigation

        an_vibration = prefManager.getData("an_vibration", an_vibration)
// endregion

// region slider
        sl_vibration = prefManager.getData("sl_vibration", sl_vibration)
// endregion

        s_firstCut = prefManager.getData("s_firstCut", s_firstCut)
        s_secondCut = prefManager.getData("s_secondCut", s_secondCut)

        AccessibleScreenValues.markPersistentDataDirty()
    }

    suspend fun save(context: Context){
        return withContext(Dispatchers.IO){
            val prefManager = PreferencesManager.getInstance(context)
//            println("saving")
            changedValuesMap.forEach { (key, value) ->
//                println("$key -> $value")
                prefManager.saveData(key, value.toString())
            }
//            println("saving complete")
        }
    }
}