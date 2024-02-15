package com.dhruv.angular_launcher.accessible_screen.components.slider.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.round
import kotlin.math.exp
import kotlin.math.pow

@Composable
fun DrawChoice (s: Int, x: Float, y: Float) {
    Text(
        modifier = AlignToMidPoint((Offset(x, y)).round()),
        text = "$s"
    )
}

@Composable
fun AllChoices (offset: Offset, height: Float, elementsCount: Int, currentSelection: Float, shift: Float){
    val hPerElement = height/(elementsCount+1)
    val first = hPerElement/2

    for ( i in 0..elementsCount) {
        val x = offset.x + gaussian(i.toFloat(), currentSelection, 0.5f) * shift
        val y = offset.y + first + hPerElement*i
        DrawChoice(i, x, y)
    }
}

@Composable
fun AlignToMidPoint(
    offset: IntOffset
): Modifier = Modifier.layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)

    val translation = offset - IntOffset (placeable.width / 2, placeable.height / 2)

    layout(placeable.width, placeable.height) {
        placeable.placeRelative(translation)
    }
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