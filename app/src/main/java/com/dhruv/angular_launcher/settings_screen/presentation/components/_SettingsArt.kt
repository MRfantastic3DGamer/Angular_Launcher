package com.dhruv.angular_launcher.settings_screen.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.accessible_screen.data.VibrationData
import com.dhruv.angular_launcher.core.AppIcon.AppIcon
import com.dhruv.angular_launcher.core.AppIcon.IconStyle
import com.dhruv.angular_launcher.data.models.IconCoordinatesGenerationInput
import java.lang.reflect.Type

object _SettingsArt {

    val defaultCornerRadius: Dp = 16.dp
    val generalPadding: Dp = 16.dp

    val DefaultEntry = mapOf<Type, @Composable (String, MutableState<Any>, Pair<Any, Any>?) -> Unit>(
        Float::class.java to { key, state, constraints->
            Column {
                H1(text = key)
                if (constraints != null){
                    Slider(
                        value = state.value.toString().toFloat(),
                        onValueChange = { state.value = it },
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

            Column (
                modifier = Modifier
            ){
                Collapsable( text = { H1(text = key) } ) {
                    Column(
                        Modifier
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(state.value.size + 20.dp),
                            Arrangement.SpaceEvenly,
                            Alignment.CenterVertically,) {
                            AppIcon(
                                pkgName = "",
                                style = state.value,
                                painter = null,
                                offset = Offset(0f, state.value.size.value),
                                selected = false,
                            )
                            AppIcon(
                                pkgName = "",
                                style = state.value,
                                painter = null,
                                offset = Offset(0f, state.value.size.value),
                                selected = true,
                            )
                        }
                        H2(text = "icon size")
                        Slider(
                            value = state.value.size.value.toString().toFloat(),
                            onValueChange = { state.value = state.value.copy(size = it.dp) },
                            valueRange = 5f..100f
                        )
                        H2(text = "border size")
                        Slider(
                            value = state.value.borderStrokeWidth.value.toString().toFloat(),
                            onValueChange = {
                                state.value = state.value.copy(borderStrokeWidth = it.dp) },
                            valueRange = 0f..20f
                        )
                        LabelForColor(
                            "border color",
                            color = state.value.borderColor,
                            onUpdate = { state.value = state.value.copy(borderColor = it) }
                        )
                        H2(text = "corner radios")
                        Slider(
                            value = state.value.cornerRadios.value.toString().toFloat(),
                            onValueChange = {
                                state.value =
                                    state.value.copy(cornerRadios = it.dp) },
                            valueRange = 0f..(state.value.size.value * 0.5f)
                        )
                        LabelForColor(
                            "Background color",
                            color = state.value.backGroundColor,
                            onUpdate = { state.value = state.value.copy(backGroundColor = it) }
                        )
                    }
                }
            }
        },
        IconCoordinatesGenerationInput::class.java to { key, state, constraints ->
            val State = state as MutableState<IconCoordinatesGenerationInput>

            Column (
                modifier = Modifier
                    .clip(RoundedCornerShape(defaultCornerRadius))
            ) {
                Collapsable({ H1(key) }) {
                    Column (
                        Modifier
                            .padding(horizontal = generalPadding)
                    ) {
                        H2(text = "starting radios")
                        Slider(
                            value = state.value.startingRadius.toFloat(),
                            onValueChange = {
                                state.value = state.value.copy(startingRadius = it.toDouble())
                            },
                            valueRange = 0f..20f
                        )
                        Divider()
                        H2(text = "radius diff per round : ${state.value.radiusDiff}")
                        Slider(
                            value = state.value.radiusDiff.toFloat(),
                            onValueChange = {
                                state.value = state.value.copy(radiusDiff = it.toDouble())
                            },
                            valueRange = 0f..20f
                        )
                        Divider()
                        H2(text = "distance between icon : ${state.value.iconDistance}")
                        Slider(
                            value = state.value.iconDistance.toFloat(),
                            onValueChange = {
                                state.value = state.value.copy(iconDistance = it.toDouble())
                            },
                            valueRange = 0f..20f
                        )
                        Divider()
                        H2(text = "rounds : ${state.value.rounds}")
                        Slider(
                            value = state.value.rounds.toFloat(),
                            onValueChange = { state.value = state.value.copy(rounds = it.toInt()) },
                            valueRange = 5f..100f,
                            steps = 96
                        )
                    }
                }
            }
        },
        VibrationData::class.java to { key, state, constraints ->
            val State = state as MutableState<VibrationData>
            // TODO: scale and time changing
            Collapsable(
                text = {
                    Row (
                        Modifier,
                        Arrangement.SpaceBetween,
                        Alignment.CenterVertically
                    ) {
                        H1(text = "should vibrate")
                        Checkbox(checked = state.value.active, onCheckedChange = { state.value = state.value.copy(active = !state.value.active) })
                    }
                },
                canOpen = state.value.active
            ) {

            }
        },
    )

    @Composable
    fun Collapsable(
        text: @Composable () -> Unit,
        canOpen: Boolean = true,
        content: @Composable () -> Unit,
    ) {
        var opened by remember { mutableStateOf(false) }

        Column (
            Modifier
                .clip(RoundedCornerShape(defaultCornerRadius))
                .background(Color.Gray)
        ) {
            Row (
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = generalPadding)
                    .clickable { opened = !opened },
                Arrangement.SpaceBetween,
                Alignment.CenterVertically,
            ) {
                text()
                Icon(
                    imageVector = if (opened) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "openDirection"
                )
            }

            AnimatedVisibility(
                visible = opened && canOpen,
            ) {
                Divider(Modifier)
                content()
            }

        }

    }

    @Composable
    private fun LabelForColor( label: String , color: Color, onUpdate: (Color)->Unit){
        val red = color.red
        val green = color.green
        val blue = color.blue

        Collapsable(text = {
            Row (
                Modifier,
                Arrangement.SpaceEvenly,
                Alignment.CenterVertically
            ) {
                H2(text = label)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .clip(RoundedCornerShape(defaultCornerRadius))
                        .background(color)
                )
            }
        }) {
            Column(modifier = Modifier.padding(generalPadding)) {
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    H2(text = "R: ${red.toString().subSequence(0, 3)}")
                    Slider(
                        value = red,
                        onValueChange = { onUpdate(color.copy(red = it)) },
                        valueRange = 0f..1f
                    )
                }
                Row {
                    H2(text = "G: ${green.toString().subSequence(0, 3)}")
                    Slider(
                        value = green,
                        onValueChange = { onUpdate(color.copy(green = it)) },
                        valueRange = 0f..1f
                    )
                }
                Row {
                    H2(text = "B: ${blue.toString().subSequence(0, 3)}")
                    Slider(
                        value = blue,
                        onValueChange = { onUpdate(color.copy(blue = it)) },
                        valueRange = 0f..1f
                    )
                }
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
    }
}

@Preview(heightDp = 1200)
@Composable
fun IconStylePrev() {
    val ICONS = remember { mutableStateOf(IconStyle()) }
    _SettingsArt.DefaultEntry[IconStyle::class.java]?.invoke("icon Style", ICONS as MutableState<Any>, null)
}

@Preview
@Composable
fun IconsCoordinatesInputPrev() {
    val COORDINATES = remember { mutableStateOf(IconCoordinatesGenerationInput()) }
    _SettingsArt.DefaultEntry[IconCoordinatesGenerationInput::class.java]?.invoke("icons coordinates generation input", COORDINATES as MutableState<Any>, null)
}


@Composable
fun H1 (text: String){
    Text(text = text, Modifier.padding( vertical = 4.dp, horizontal = 8.dp), color = Color.White, fontSize = TextUnit(25f, TextUnitType.Sp))
}

@Composable
fun H2 (text: String){
    Text(text = text, Modifier.padding( vertical = 2.dp, horizontal = 8.dp), color = Color.White, fontSize = TextUnit(18f, TextUnitType.Sp))
}

@Composable
fun H3 (text: String){
    Text(text = text)
}