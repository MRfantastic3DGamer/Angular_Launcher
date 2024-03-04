package com.dhruv.angular_launcher.accessible_screen.components.slider

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import androidx.core.math.MathUtils.clamp
import com.dhruv.angular_launcher.accessible_screen.components.slider.data.SliderPersistentData
import com.dhruv.angular_launcher.accessible_screen.components.slider.data.SliderValues
import com.dhruv.angular_launcher.accessible_screen.data.SelectionData
import com.dhruv.angular_launcher.utils.ScreenUtils
import java.lang.Float.max
import java.lang.Float.min
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

object SliderFunctions {
    fun SliderPath(
        offset: Offset,
        persistentData: SliderPersistentData,
        selectionPos: Float,
    ): Path {

        val triggerOffset = offset
        val triggerSize = Size(ScreenUtils.dpToF(persistentData.width), ScreenUtils.dpToF(persistentData.height))

        val maxAngle        = 180f
        val quality_trigg   = persistentData.triggerCurveEdgeCount

        val radius_1 = persistentData.selectionRadios
        val height = ScreenUtils.dpToF(persistentData.height)

// region trigger arc

        val triggerArcTop       = mutableListOf<Offset>()
        val triggerArcBot       = mutableListOf<Offset>()
        val triggerArcRadius    = triggerSize.width/2
        val deltaAngle_trigg    = (maxAngle / quality_trigg) * (PI / 180).toFloat()
        val triggerTopArcMid    = triggerOffset + Offset(triggerArcRadius, min(0f,  selectionPos - radius_1 + triggerArcRadius))
        val triggerDownArcMid   = triggerOffset + Offset(triggerArcRadius, max(height, selectionPos + radius_1 - triggerArcRadius))

        repeat(quality_trigg) {
            val angle = deltaAngle_trigg * it
            triggerArcTop.add(triggerTopArcMid  + Offset(-(cos(angle) * triggerArcRadius), -(sin(angle) * triggerArcRadius)))
            triggerArcBot.add(triggerDownArcMid + Offset( (cos(angle) * triggerArcRadius),  (sin(angle) * triggerArcRadius)))
        }

// endregion

        val triggerOpenPath = Path()

        triggerOpenPath.moveTo(offset.x - persistentData.sidePadding, offset.y + selectionPos - persistentData.selectionRadios)

// selection arc
        triggerOpenPath.addArc(
            Rect(
                topLeft = triggerOffset + Offset(-persistentData.sidePadding-persistentData.selectionRadios, selectionPos-persistentData.selectionRadios),
                bottomRight = triggerOffset + Offset(-persistentData.sidePadding+persistentData.selectionRadios, selectionPos+persistentData.selectionRadios)
            ),
            startAngleDegrees =  90f,
            sweepAngleDegrees = 180f,
        )

        val minY        = offset.y + selectionPos - persistentData.selectionRadios
        val maxY        = offset.y + selectionPos + persistentData.selectionRadios

        triggerOpenPath.lineTo(triggerArcTop.first().x, triggerOffset.y + selectionPos-persistentData.selectionRadios )

// top arc
        for (i in 0 until triggerArcTop.size/2)
            triggerOpenPath.lineTo(triggerArcTop[i].x, min(minY, triggerArcTop[i].y))
        for (i in triggerArcTop.size/2+1 until triggerArcTop.size)
            triggerOpenPath.lineTo(triggerArcTop[i].x, triggerArcTop[i].y)

// down arc
        for (i in 0 until triggerArcBot.size/2)
            triggerOpenPath.lineTo(triggerArcBot[i].x, triggerArcBot[i].y)
        for (i in triggerArcBot.size/2+1 until triggerArcBot.size)
            triggerOpenPath.lineTo(triggerArcBot[i].x, max(maxY, triggerArcBot[i].y))

        triggerOpenPath.lineTo(triggerArcTop.first().x, triggerOffset.y + selectionPos+persistentData.selectionRadios )

        triggerOpenPath.close()
        return triggerOpenPath
    }

    fun GetSliderPositionY (touchPosY: Float, sliderHeight: Float, sliderPosY: Float): Float {
        val topPositionY =
            if (touchPosY < sliderPosY) touchPosY
            else if (touchPosY > sliderPosY + sliderHeight) touchPosY - sliderHeight
            else sliderPosY
        return topPositionY
    }

    fun calculateSliderPosition (yPos: Float): Offset {
        return Offset(
            ScreenUtils.fromRight(ScreenUtils.dpToF(SliderValues.GetPersistentData.value?.width ?: 10.dp)),
            yPos
        )
    }

    fun calculateCurrentSelection(numberOfElements: Int, height: Float, touchPosY: Float): SelectionData {
        if (numberOfElements <= 0) return SelectionData(-1, 0f)
        val hPerElement = height/(numberOfElements)
        if (numberOfElements == 1) return SelectionData(0, hPerElement/2)
        val selection_i = clamp((touchPosY / hPerElement).toInt(), 0, numberOfElements-1)
        val selectionPos = selection_i*hPerElement + hPerElement/2
        return SelectionData(
            index = selection_i,
            posY = selectionPos,
        )
    }
}