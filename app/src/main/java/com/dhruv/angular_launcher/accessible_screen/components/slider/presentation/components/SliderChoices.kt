package com.dhruv.angular_launcher.accessible_screen.components.slider.presentation.components

import android.content.res.Resources
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.round
import com.dhruv.angular_launcher.data.enums.SelectionMode
import com.dhruv.angular_launcher.utils.MathUtils.gaussian
import com.dhruv.angular_launcher.utils.ScreenUtils

@Composable
fun DrawTextChoice (s: String, x: Float, y: Float) {
    Text(
        modifier = Modifier.alignToMidPoint((Offset(x, y)).round()),
        text = s
    )
}

@Composable
fun DrawGroupIconChoice(key: Int, x: Float, y: Float, resources: Resources, size: Dp) {
    val image = BitmapFactory.decodeResource(resources, key).asImageBitmap()
    Image(
        bitmap = image,
        contentDescription = key.toString(),
        Modifier
            .size(size)
            .alignToMidPoint((Offset(x, y)).round()),
        Alignment.Center,
        ContentScale.Fit,
        colorFilter = ColorFilter.lighting(Color.Black, Color.Black)
    )
}

fun GroupIconPositions(
    offset: Offset,
    height: Float,
    width: Dp,
    optionsCount: Int,
    currentSelection: Float,
    shift: Float,
    ): List<Offset> {
    val hPerElement = height/(optionsCount)
    val first = hPerElement/2
    val commonX = offset.x + ScreenUtils.dpToF(width)/2
    return (0 until optionsCount).map { i ->
        val x = commonX - gaussian(i.toFloat(), currentSelection, 0.2f) * (shift - 40f)
        val y = offset.y + first + hPerElement*i
        Offset(x,y)
    }
}

@Composable
fun AllChoices(
    allOptions: List<String>,
    offsets: List<Offset>,
    selectionMode: SelectionMode,
    width: Dp
) {
    val context = LocalContext.current
    when (selectionMode) {
        SelectionMode.NotSelected,
        SelectionMode.BySearch -> {}
        SelectionMode.ByAlphabet -> {
            allOptions.forEachIndexed { index, s ->
                DrawTextChoice(s, offsets[index].x, offsets[index].y)
            }
        }
        SelectionMode.ByGroup -> {
            allOptions.forEachIndexed { index, n ->
                DrawGroupIconChoice(key = n.toInt(), x = offsets[index].x, y = offsets[index].y, resources = context.resources, size = width)
            }
        }
    }
}

@Composable
fun Modifier.alignToMidPoint(
    offset: IntOffset
): Modifier {
    return this
        .offset { offset }
        .layout { measurable, constraints ->
            val placeable = measurable.measure(constraints)

            val translation = -IntOffset(placeable.width / 2, placeable.height / 2)

            layout(placeable.width, placeable.height) {
                placeable.placeRelative(translation)
            }
        }
}


