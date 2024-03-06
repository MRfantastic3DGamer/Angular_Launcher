package com.dhruv.angular_launcher.settings_screen.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

@Composable
fun tabButton(
    text: String,
    selected: Boolean,
    onTap: ()->Unit
){
    val bgColor by animateColorAsState(
        targetValue = when (selected) {
            true -> Color.White
            false -> Color.Black
        }
    )
    val txtColor by animateColorAsState(
        targetValue = when (selected) {
            true -> Color.Black
            false -> Color.White
        }
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(100))
            .background(bgColor)
            .clickable { onTap() }
    ) {
        Text(
            text = text,
            modifier = Modifier
                .vertical(true)
                .rotate(-90f)
                .padding(16.dp),
            style = TextStyle(
                color = txtColor,
                fontSize = TextUnit(20f, TextUnitType.Sp),
                fontWeight = FontWeight.ExtraBold
            )
        )
    }
}

fun Modifier.vertical(enabled: Boolean = true) =
    if (enabled)
        layout { measurable, constraints ->
            val placeable = measurable.measure(constraints.copy(maxWidth = Int.MAX_VALUE))
            layout(placeable.height, placeable.width) {
                placeable.place(
                    x = -(placeable.width / 2 - placeable.height / 2),
                    y = -(placeable.height / 2 - placeable.width / 2)
                )
            }
        } else this