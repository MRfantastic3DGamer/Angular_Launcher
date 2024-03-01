package com.dhruv.angular_launcher.accessible_screen.components.slider.presentation.components

import android.content.res.Resources
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import com.dhruv.angular_launcher.data.enums.SelectionMode
import kotlin.math.exp
import kotlin.math.pow

@Composable
fun DrawTextChoice (s: String, x: Float, y: Float) {
    Text(
        modifier = Modifier.AlignToMidPoint((Offset(x, y)).round()),
        text = s
    )
}

@Composable
fun DrawGroupIconChoice(key: Int, x: Float, y: Float, resources: Resources) {
    val image = BitmapFactory.decodeResource(resources, key).asImageBitmap()
    Image(
        bitmap = image,
        contentDescription = key.toString(),
        Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(Color.White)
            .AlignToMidPoint((Offset(x, y)).round()),
        Alignment.Center,
        ContentScale.Fit,
    )
}

@Composable
fun AllChoices (
    offset: Offset,
    height: Float,
    allOptions: List<String>,
    currentSelection: Float,
    shift: Float,
    selectionMode: SelectionMode
){
    val hPerElement = height/(allOptions.size)
    val first = hPerElement/2
    val context = LocalContext.current

    allOptions.forEachIndexed{ i, n ->
        val x = offset.x + gaussian(i.toFloat(), currentSelection, 0.5f) * shift
        val y = offset.y + first + hPerElement*i
        when (selectionMode) {
            SelectionMode.NotSelected -> {}
            SelectionMode.ByAlphabet -> {
                DrawTextChoice(n, x, y)
            }
            SelectionMode.BySearch -> {}
            SelectionMode.ByGroup -> {
                DrawGroupIconChoice(key = n.toInt(), x = x, y = y, resources = context.resources)
            }
        }
    }
}

@Composable
fun Modifier.AlignToMidPoint(
    offset: IntOffset
): Modifier {
    this.layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)

        val translation = offset - IntOffset(placeable.width / 2, placeable.height / 2)

        layout(placeable.width, placeable.height) {
            placeable.placeRelative(translation)
        }
    }
    return this
}


/**
 * @param a -> lifted point
 * @param k -> steepness
 */
fun gaussian(x: Float, a: Float, k: Float): Float {
    return 1 - exp(-((x - a).pow(2) / (2 * k.pow(2))))
}


//@Composable
//fun allAlphabets (choiceOffsets: MutableList<State<Float>>, offset: Offset, height: Float, elementsCount: Int) {
//    val hPerElement = height/(elementsCount+1)
//    val first = hPerElement/2
//
//    for ( i in 0..elementsCount) {
//        val x = offset.x + choiceOffsets[i].value
//        val y = offset.y + first + hPerElement*i
//        animatedAlphabet(i, x, y)
//    }
//}

//@Composable
//fun getAnimatedAlphabetOffset(
//    count: Int,
//    height: Float,
//    width: Float,
//    alphabetSideFloat: Float,
//    selectedElement: Int,
//
//    ): MutableList<State<Float>> {
//
//    val alphabetOffsets = mutableListOf<State<Float>>()
//
//    for (element in 0..count) {
//        alphabetOffsets.add(
//            animateFloatAsState(
//            targetValue = if (element == selectedElement) (-alphabetSideFloat) else (width/2),
//            label = "animateAlphabetOffset{$element}",
//            animationSpec = tween(durationMillis = 50)
//        )
//        )
//    }
//    return alphabetOffsets
//}