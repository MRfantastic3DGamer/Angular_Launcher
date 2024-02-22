package com.dhruv.angular_launcher.settings_module.prefferences.values

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorLooks
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigationFunctions
import com.dhruv.angular_launcher.accessible_screen.data.AccessibleScreenValues
import com.dhruv.angular_launcher.accessible_screen.data.VibrationData
import com.dhruv.angular_launcher.settings_module.prefferences.PreferencesManager

/**
 * sl -> -slider- on right side of screen
 * t  -> -theme-
 * fc -> -fluid cursor-
 * an -> -app navigation-
 */
object PrefValues {


    // region app navigation
    var an_iconSize: Float = 100f
    var an_enlargeSelectedIconBy: Float = 0.2f
    var an_shouldBlur: Boolean = false
    var an_blurAmount: Float = 0f
    var an_tint: Color = Color.Black

    var an_startingRadius1:Double = 250.0
    var an_radiusDiff1:Double = 150.0
    var an_iconDistance1:Double = 150.0
    var an_rounds1: Int = 20
    val an_option1: RadialAppNavigationFunctions.IconCoordinatesGenerationInput
        get() = RadialAppNavigationFunctions.IconCoordinatesGenerationInput(an_startingRadius1, an_radiusDiff1, an_iconDistance1, an_rounds1)
    var an_startingRadius2:Double = 250.0
    var an_radiusDiff2:Double = 125.0
    var an_iconDistance2:Double = 125.0
    var an_rounds2: Int = 20
    val an_option2: RadialAppNavigationFunctions.IconCoordinatesGenerationInput
        get() = RadialAppNavigationFunctions.IconCoordinatesGenerationInput(an_startingRadius2, an_radiusDiff2, an_iconDistance2, an_rounds2)
    var an_startingRadius3:Double = 250.0
    var an_radiusDiff3:Double = 100.0
    var an_iconDistance3:Double = 100.0
    var an_rounds3: Int = 20
    val an_option3: RadialAppNavigationFunctions.IconCoordinatesGenerationInput
        get() = RadialAppNavigationFunctions.IconCoordinatesGenerationInput(an_startingRadius3, an_radiusDiff3, an_iconDistance3, an_rounds3)
    var an_startingRadius4:Double = 250.0
    var an_radiusDiff4:Double = 75.0
    var an_iconDistance4:Double = 75.0
    var an_rounds4: Int = 30
    val an_option4: RadialAppNavigationFunctions.IconCoordinatesGenerationInput
        get() = RadialAppNavigationFunctions.IconCoordinatesGenerationInput(an_startingRadius4, an_radiusDiff4, an_iconDistance4, an_rounds4)
    var an_startingRadius5:Double = 250.0
    var an_radiusDiff5:Double = 50.0
    var an_iconDistance5:Double = 50.0
    var an_rounds5: Int = 40
    val an_option5: RadialAppNavigationFunctions.IconCoordinatesGenerationInput
        get() = RadialAppNavigationFunctions.IconCoordinatesGenerationInput(an_startingRadius5, an_radiusDiff5, an_iconDistance5, an_rounds5)
    var an_vibrationActive: Boolean = false
    var an_vibrationAmount: Float = 1f
    var an_vibrationTime: Float = 1f
    val an_vibration: VibrationData
        get() = VibrationData(an_vibrationActive, an_vibrationAmount, an_vibrationTime)
    // endregion

    // region fluid cursor
    val fc_animationSpeed: Float = 1f
    val fc_fluidCursorLooks: FluidCursorLooks = FluidCursorLooks() // TODO: convert it into a derived Getter
    // endregion

    // region slider

    val sl_vibrationTime: Float = 1f
    val sl_vibrationAmount: Float = 1f
    val sl_vibrateOnSelectionChange: Boolean = false
    val sl_tint: Color = Color.Black
    val sl_shouldBlur: Boolean = false
    val sl_selectionRadios: Float = 100f
    val sl_blurAmount: Float = 1f
    var sl_width: Float = 100f
    var sl_height: Float = 1300f
    var sl_topPadding: Float = 50f
    var sl_downPadding: Float = 50f
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