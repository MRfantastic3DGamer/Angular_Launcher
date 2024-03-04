package com.dhruv.angular_launcher.accessible_screen.components.slider.presentation.components

import android.content.res.Resources
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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
            .alignToMidPoint((Offset(x, y)).round())
            .clip(CircleShape)
            .background(Color.White),
        Alignment.Center,
        ContentScale.Fit,
    )
}

@Composable
fun AllChoices (
    offset: Offset,
    height: Float,
    width: Dp,
    allOptions: List<String>,
    currentSelection: Float,
    shift: Float,
    selectionMode: SelectionMode
){
    val hPerElement = height/(allOptions.size)
    val first = hPerElement/2
    val context = LocalContext.current

    val commonX = offset.x + ScreenUtils.dpToF(width)/2

    allOptions.forEachIndexed{ i, n ->
        val x = commonX - gaussian(i.toFloat(), currentSelection, 0.2f) * shift
        val y = offset.y + first + hPerElement*i
        when (selectionMode) {
            SelectionMode.NotSelected -> {}
            SelectionMode.ByAlphabet -> {
                DrawTextChoice(n, x, y)
            }
            SelectionMode.BySearch -> {}
            SelectionMode.ByGroup -> {
                DrawGroupIconChoice(key = n.toInt(), x = x, y = y, resources = context.resources, size = width)
            }
        }
    }
}

@Composable
fun Modifier.alignToMidPoint(
    offset: IntOffset
): Modifier {
    return this.offset { offset }.layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)

        val translation = - IntOffset(placeable.width / 2, placeable.height / 2)

        layout(placeable.width, placeable.height) {
            placeable.placeRelative(translation)
        }
    }
}


