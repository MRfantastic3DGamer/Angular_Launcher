package com.dhruv.angular_launcher.settings_module

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.dhruv.angular_launcher.settings_module.prefferences.values.PrefValues
import java.lang.reflect.Type

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
    map: Map<String, Type>,
    constraints: Map<String, Pair<Float, Float>>,
    drawing: Map<Type, @Composable (String, Any, Pair<Float, Float>?, (Any)->Unit)->Unit>,
    specialDrawing: Map<String, @Composable (String, Any, Pair<Float, Float>?, (Any)->Unit)->Unit>,
    saveValues: MutableMap<String, Any>,
){
    val context = LocalContext.current
    LazyColumn {
        items(map.keys.toList()) { key ->
            val variable = PrefValues::class.java.getDeclaredField(key)
            variable.isAccessible = true

            var value by remember { mutableStateOf(variable.get(PrefValues)) }
            saveValues[key] = value
            if (specialDrawing.containsKey(key)) {
                specialDrawing[key]?.invoke(key, value, constraints[key]) { n ->
                    value = n
                    saveValues[key] = value
                }
            } else {
                drawing[map[key]]?.invoke(key, value, constraints[key]) { n ->
                    value = n
                    saveValues[key] = value
                }
            }
        }
    }
}