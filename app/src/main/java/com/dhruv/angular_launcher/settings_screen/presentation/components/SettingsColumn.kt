package com.dhruv.angular_launcher.settings_screen.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.dhruv.angular_launcher.database.prefferences.values.PrefValues
import java.lang.reflect.Type


data class EntryData(
    val label: String,
    val data: MutableState<Any>,
    val type: Type,
    val constraints: Pair<Any, Any>? = null,
)
data class SettingsColumnData(
    val heading: String,
    val subHeading: String?,
    val values: List<EntryData>
)

/**
 * It is used to draw and save a list of values that must be predefined in [PrefValues]
 *
 * @param map -> key(variable name) value(data type)
 * @param drawing -> default way of drawing button for each type
 * @param saveValues -> used to update values
 * @param specialDrawing -> unique way to draw for special case/key
 * @param constraints -> for int/float values
 */

@Composable
fun SettingsColumn (
    data: SettingsColumnData,
    entryForType: Map<Type, @Composable (String, MutableState<Any>, Pair<Any, Any>?) -> Unit>
){
    Column (
        Modifier.fillMaxWidth()
    ) {

        Text(text = data.heading)

        data.values.forEach { it ->
            entryForType[it.type]?.invoke(it.label, it.data, it.constraints)
        }
    }
}