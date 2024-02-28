package com.dhruv.angular_launcher.settings_screen.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.accessible_screen.data.VibrationData
import com.dhruv.angular_launcher.core.AppIcon.AppIcon
import com.dhruv.angular_launcher.core.AppIcon.IconStyle
import com.dhruv.angular_launcher.data.models.IconCoordinatesGenerationInput
import java.lang.reflect.Type

object _SettingsArt {
    val DefaultEntry = mapOf<Type, @Composable (String, MutableState<Any>, Pair<Any, Any>?) -> Unit>(
        Float::class.java to { key, state, constraints->
            Column {
                H1(text = key)
                if (constraints != null){
                    Slider(
                        value = state.value.toString().toFloat(),
                        onValueChange = { state.value = it.toFloat() },
                        valueRange = constraints.first.toString().toFloat()..constraints.second.toString().toFloat(),
                    )
                }
                else{
                    TextField(value = state.value.toString(), onValueChange = { state.value = it.toFloat() }, maxLines = 1)
                }
            }
        },
        Int::class.java to { key, state, constraints ->
            Column {
                H1(text = key)
                if (constraints != null){
                    Slider(
                        value = state.value.toString().toFloat(),
                        onValueChange = { state.value = it.toInt() },
                        valueRange = constraints.first.toString().toFloat()..constraints.second.toString().toFloat(),
                    )
                }
                else{
                    TextField(value = state.value.toString(), onValueChange = { state.value = it.toInt() }, maxLines = 1)
                }
            }
        },
        IconStyle::class.java to { key, state, constraints ->
            val State = state as MutableState<IconStyle>
            Column {
                H1(text = "Icon style")
                Row (
                    Modifier.fillMaxWidth().height(state.value.size + 20.dp),
                    Arrangement.SpaceEvenly,
                    Alignment.CenterVertically,
                ){
                    AppIcon("", state.value, null, Offset(0f, state.value.size.value), false)
                    AppIcon("", state.value, null, Offset(0f, state.value.size.value), true)
                }
                H1(text = "icon size")
                Slider(
                    value = state.value.size.value.toString().toFloat(),
                    onValueChange = { state.value = state.value.copy(size = it.dp) },
                    valueRange = 5f..100f
                )
                H1(text = "border size")
                Slider(
                    value = state.value.borderStrokeWidth.value.toString().toFloat(),
                    onValueChange = { state.value = state.value.copy(borderStrokeWidth = it.dp) },
                    valueRange = 0f..20f
                )
                H1(text = "corner radios")
                Slider(
                    value = state.value.cornerRadios.value.toString().toFloat(),
                    onValueChange = { state.value = state.value.copy(cornerRadios = (state.value.size.value * 0.5 * it).dp) },
                    valueRange = 0f..1f
                )
            }
        },
        IconCoordinatesGenerationInput::class.java to { key, state, constraints ->
            val State = state as MutableState<IconCoordinatesGenerationInput>
            Column {
                H1(text = "starting radious")
                Slider(
                    value = state.value.startingRadius.toString().toFloat(),
                    onValueChange = { state.value = state.value.copy(iconDistance = it.toDouble()) },
                    valueRange = 0f..20f
                )
            }
        },
        VibrationData::class.java to { key, state, constraints ->
            val State = state as MutableState<VibrationData>
            H1(text = "should vibrate")
            Checkbox(checked = state.value.active, onCheckedChange = { state.value = state.value.copy(active = !state.value.active) })


        },
    )

    @Composable
    private fun H1 (text: String){
        Text(text = text)
    }

    private fun LabelForFloat(){

    }

    @Composable
    private fun LabelForColor( color: Color, onUpdate: (Color)->Unit){
        val red = color.red
        val green = color.green
        val blue = color.blue

        Box(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color(red = red / 255f, green = green / 255f, blue = blue / 255f))
                )

                Text(text = "Red: ${red.toInt()}", modifier = Modifier.padding(bottom = 8.dp))
                Slider(
                    value = red,
                    onValueChange = { onUpdate(color.copy(red = it)) },
                    valueRange = 0f..255f
                )

                Text(text = "Green: ${green.toInt()}", modifier = Modifier.padding(vertical = 8.dp))
                Slider(
                    value = green,
                    onValueChange = { onUpdate(color.copy(green = it)) },
                    valueRange = 0f..255f
                )

                Text(text = "Blue: ${blue.toInt()}", modifier = Modifier.padding(top = 8.dp))
                Slider(
                    value = blue,
                    onValueChange = { onUpdate(color.copy(blue = it)) },
                    valueRange = 0f..255f
                )
            }
        }
    }
}

@Preview
@Composable
fun Pr() {
    Column {
        val FS = remember { mutableStateOf(10f) }
        _SettingsArt.DefaultEntry[Float::class.java]?.invoke("float", FS as MutableState<Any>, null)
        val IS = remember { mutableStateOf(10) }
        _SettingsArt.DefaultEntry[Int::class.java]?.invoke("int", IS as MutableState<Any>, null)
        val ICONS = remember { mutableStateOf(IconStyle()) }
        _SettingsArt.DefaultEntry[IconStyle::class.java]?.invoke("icon Style", ICONS as MutableState<Any>, null)
        val COORDINATES = remember { mutableStateOf(IconCoordinatesGenerationInput()) }
        _SettingsArt.DefaultEntry[IconCoordinatesGenerationInput::class.java]?.invoke("icons coordinates generation input", COORDINATES as MutableState<Any>, null)
    }
}