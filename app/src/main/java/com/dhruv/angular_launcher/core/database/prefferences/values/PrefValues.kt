package com.dhruv.angular_launcher.core.database.prefferences.values

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorLooks
import com.dhruv.angular_launcher.accessible_screen.data.AccessibleScreenValues
import com.dhruv.angular_launcher.accessible_screen.data.VibrationData
import com.dhruv.angular_launcher.data.models.IconCoordinatesGenerationInput
import com.dhruv.angular_launcher.core.AppIcon.IconStyle
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

    var wallpaper: String = ""

    // endregion

    // region app navigation
    var an_iconStyle: IconStyle = IconStyle()
    var an_enlargeSelectedIconBy: Float = 0.2f
    var an_shouldBlur: Boolean = false
    var an_blurAmount: Float = 0f
    var an_tint: Color = Color.Black

    var an_option1: IconCoordinatesGenerationInput = IconCoordinatesGenerationInput(250.0, 150.0, 150.0, 20)
    var an_option2: IconCoordinatesGenerationInput = IconCoordinatesGenerationInput(250.0, 125.0, 125.0, 20)
    var an_option3: IconCoordinatesGenerationInput = IconCoordinatesGenerationInput(250.0, 100.0, 100.0, 20)
    var an_option4: IconCoordinatesGenerationInput = IconCoordinatesGenerationInput(250.0, 75.0, 75.0, 30)
    var an_option5: IconCoordinatesGenerationInput = IconCoordinatesGenerationInput(250.0, 50.0, 50.0, 40)

    var an_vibration: VibrationData = VibrationData(false, 1f, 1f)
    // endregion

    // region fluid cursor
    var fc_animationSpeed: Float = 1f
    var fc_fluidCursorLooks: FluidCursorLooks = FluidCursorLooks()
    // endregion

    // region slider

    var sl_vibration: VibrationData = VibrationData()
    var sl_tint: Color = Color.Black
    var sl_shouldBlur: Boolean = false
    var sl_selectionRadios: Float = 100f
    var sl_blurAmount: Float = 1f
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

    suspend fun loadAllValues(context: Context){
        val prefManager = PreferencesManager.getInstance(context)

// region theme
        wallpaper = prefManager.getData("wallPaper", wallpaper)
// endregion

// region app navigation
        an_iconStyle = prefManager.getData("an_iconStyle", an_iconStyle)
        an_enlargeSelectedIconBy = prefManager.getData("an_enlargeSelectedIconBy", an_enlargeSelectedIconBy)
        an_shouldBlur = prefManager.getData("an_shouldBlur", an_shouldBlur)
        an_blurAmount = prefManager.getData("an_blurAmount", an_blurAmount)
        an_tint = prefManager.getData("an_tint", an_tint)
        an_option1 = prefManager.getData("an_option1", an_option1)
        an_option2 = prefManager.getData("an_option2", an_option2)
        an_option3 = prefManager.getData("an_option3", an_option3)
        an_option4 = prefManager.getData("an_option4", an_option4)
        an_option5 = prefManager.getData("an_option5", an_option5)

        an_vibration = prefManager.getData("an_vibration", an_vibration)
// endregion

// region fluid cursor
        fc_animationSpeed = prefManager.getData("fc_animationSpeed", fc_animationSpeed)
        fc_fluidCursorLooks = prefManager.getData("fc_fluidCursorLooks", fc_fluidCursorLooks)

// endregion

// region slider
        sl_vibration = prefManager.getData("sl_vibration", sl_vibration)
        sl_tint = prefManager.getData("sl_tint", sl_tint)
        sl_shouldBlur = prefManager.getData("sl_shouldBlur", sl_shouldBlur)
        sl_selectionRadios = prefManager.getData("sl_selectionRadios", sl_selectionRadios)
        sl_blurAmount = prefManager.getData("sl_blurAmount", sl_blurAmount)
        sl_width = prefManager.getData("sl_width", sl_width)
        sl_height = prefManager.getData("sl_height", sl_height)
        sl_topPadding = prefManager.getData("sl_topPadding", sl_topPadding)
        sl_downPadding = prefManager.getData("sl_downPadding", sl_downPadding)
        sl_triggerCurveEdgeCount = prefManager.getData("sl_triggerCurveEdgeCount", sl_triggerCurveEdgeCount)
        sl_selectionCurveOffset = prefManager.getData("sl_selectionCurveOffset", sl_selectionCurveOffset)
// endregion

        s_firstCut = prefManager.getData("s_firstCut", s_firstCut)
        s_secondCut = prefManager.getData("s_secondCut", s_secondCut)

        AccessibleScreenValues.markPersistentDataDirty()
    }

    suspend fun save(context: Context){
        return withContext(Dispatchers.IO){
            val prefManager = PreferencesManager.getInstance(context)
            println("saving")
            changedValuesMap.forEach { (key, value) ->
                println("$key -> $value")
                prefManager.saveData(key, value.toString())
            }
            println("saving complete")
        }
    }
}