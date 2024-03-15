package com.dhruv.angular_launcher.core.database.prefferences.values

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorLooks
import com.dhruv.angular_launcher.accessible_screen.data.AccessibleScreenValues
import com.dhruv.angular_launcher.accessible_screen.data.VibrationData
import com.dhruv.angular_launcher.core.appIcon.IconStyle
import com.dhruv.angular_launcher.core.database.prefferences.PreferencesManager
import com.dhruv.angular_launcher.data.models.IconCoordinatesGenerationInput
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

    var t_shader: String = """
        void main() {
            float value = 1./distance(gl_FragCoord.xy, u_mouse) * 1.2;
            vec2 t_pos = vec2(0., 0.);
            float t_dist = 100000.;
        
            for(int i=0;i<MAX_ICONS;++i)
            {
                if (u_positions_X[i] == -10000.)
                    break;
                t_pos = vec2(u_positions_X[i], u_positions_Y[i]);
                t_dist = min(t_dist, distance(gl_FragCoord.xy, t_pos));
            }
            value += 1./t_dist;
        
            float radius = 1./50.0;
        
            float insideCircle = step(value, radius);
        
            vec4 color = insideCircle * vec4(0.0, 0.0, 0.0, 0.0);
            color += (1.0 - insideCircle) * vec4(1.0, 1.0, 1.0, 1.0);
        
            gl_FragColor = color;
        }
    """
    var t_bg_path: String = ""
    var t_bg_path_1: String = ""
    var t_bg_path_2: String = ""
    var t_bg_path_3: String = ""
    var t_bg_path_4: String = ""
    var t_bg_path_5: String = ""

    // endregion

    // region app navigation
    var an_iconStyle: IconStyle = IconStyle(
        size = 30.dp,
        backGroundColor = Color.White,
        borderColor = Color.Black,
        borderStrokeWidth = 2.dp,
        cornerRadios = 5.dp,
    )
    var an_selectedIconStyle: IconStyle = IconStyle(
        size = 65.dp,
        backGroundColor = Color.Black,
        borderColor = Color.White,
        borderStrokeWidth = 6.dp,
        cornerRadios = 100.dp,
    )
    var an_shouldBlur: Boolean = false
    var an_blurAmount: Float = 0f
    var an_tint: Color = Color.Black

    var an_option1: IconCoordinatesGenerationInput = IconCoordinatesGenerationInput(250.0, 175.0, 225.0, 20)
    var an_option2: IconCoordinatesGenerationInput = IconCoordinatesGenerationInput(250.0, 150.0, 200.0, 20)
    var an_option3: IconCoordinatesGenerationInput = IconCoordinatesGenerationInput(250.0, 125.0, 175.0, 20)
    var an_option4: IconCoordinatesGenerationInput = IconCoordinatesGenerationInput(250.0, 100.0, 150.0, 30)
    var an_option5: IconCoordinatesGenerationInput = IconCoordinatesGenerationInput(250.0, 75.0, 125.0, 40)

    var an_vibration: VibrationData = VibrationData(true, 1f, 1f)
    // endregion

    // region fluid cursor
    var fc_animationSpeed: Float = 1f
    var fc_fluidCursorLooks: FluidCursorLooks = FluidCursorLooks()
    // endregion

    // region slider

    var sl_vibration: VibrationData = VibrationData(active = true)
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

    fun loadAllValues(context: Context){
        val prefManager = PreferencesManager.getInstance(context)

// region theme
        t_shader = prefManager.getData("t_shader", t_shader)
        t_bg_path = prefManager.getData("t_bg_path", t_bg_path)
        t_bg_path_1 = prefManager.getData("t_bg_path_1", t_bg_path_1)
        t_bg_path_2 = prefManager.getData("t_bg_path_2", t_bg_path_2)
        t_bg_path_3 = prefManager.getData("t_bg_path_3", t_bg_path_3)
        t_bg_path_4 = prefManager.getData("t_bg_path_4", t_bg_path_4)
        t_bg_path_5 = prefManager.getData("t_bg_path_5", t_bg_path_5)
// endregion

// region app navigation
        an_iconStyle = prefManager.getData("an_iconStyle", an_iconStyle)
        an_selectedIconStyle = prefManager.getData("an_selectedIconStyle", an_selectedIconStyle)
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