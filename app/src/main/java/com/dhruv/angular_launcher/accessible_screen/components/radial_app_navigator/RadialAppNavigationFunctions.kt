package com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator

import androidx.compose.ui.geometry.Offset
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data.IconCoordinate
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data.RadialAppNavigatorData
import com.dhruv.angular_launcher.accessible_screen.components.slider.data.SliderValues
import com.dhruv.angular_launcher.utils.MathUtils
import com.dhruv.angular_launcher.utils.ScreenUtils

object RadialAppNavigationFunctions {
    fun getRadialAppNavigatorData (
        selectionOffsetX: Float,
        selectionOffsetY: Float,
        sliderWidth: Float,
        selection_i: Int,
    ): RadialAppNavigatorData {
        return RadialAppNavigatorData(
            currentSelectionIndex = selection_i,
            center = Offset(ScreenUtils.fromRight(sliderWidth+ selectionOffsetX), selectionOffsetY),
        )
    }

    fun getPositionOnCircle(coordinate: IconCoordinate): Offset {
        return MathUtils.getPositionOnCircle(coordinate.distance, -coordinate.angle)
    }

    fun getUsableOffsets (list: List<List<Offset>>, center: Offset, count: Int): List<Offset> {
        val sliderOffset = SliderValues.GetSliderData.value!!.sliderOffset
        val sliderHeight = ScreenUtils.dpToF(SliderValues.GetPersistentData.value!!.height)
        val top = sliderOffset.y + 50f
        val bot = sliderOffset.y + sliderHeight - 50f
        val left= 100f
        val iconSizeOffset = Offset(2.5f, 2.5f)


        fun getResult (iconSizePreset :Int): MutableList<Offset> {
            val res = mutableListOf<Offset>()
            var j = 0
            for (i in 0 until count){
                if (j !in list[iconSizePreset].indices) {
                    if (iconSizePreset >= list.size-1)
                        return res
                    return getResult(iconSizePreset+1)
                }
                var c = center + list[iconSizePreset][j] - iconSizeOffset
                while ( !( (c.x > left) && (c.y > top) && (c.y < bot) )){
                    j++
                    if (j !in list[iconSizePreset].indices) {
                        if (iconSizePreset >= list.size-1)
                            return res
                        return getResult(iconSizePreset+1)
                    }
                    c = center + list[iconSizePreset][j] - iconSizeOffset
                }
                j++
                res.add(c)
            }
            return res
        }

        val res = getResult (0)
        println("${res.size} -> $count")
        return res
    }

    data class IconCoordinatesGenerationInput(
        val startingRadius:Double = 250.0,
        val radiusDiff:Double = 150.0,
        val iconDistance:Double = 150.0,
        val rounds: Int = 20,
    )
    /**
     * all possible coordinates for icons for [0,0] as its center
     * @param iconOffset
     * @param indexToRC index to row and column
     */
    data class IconCoordinatesResult(
        val iconCoordinates: List<IconCoordinate>,
        val iconOffset: List<Offset>,
        val indexToRC: List<Pair<Int,Int>>,
        val coordinateToIndex: List<List<Int>>
    )
    fun generateIconCoordinates (input: IconCoordinatesGenerationInput): IconCoordinatesResult {
        val startingRadius = input.startingRadius
        val radiusDiff = input.radiusDiff
        val iconDistance = input.iconDistance
        val rounds = input.rounds
        val startingAngle:Double = -90.0
        val endAngle = 90.0

        var row = 0
        var radius = startingRadius
        val iconCoordinates = mutableListOf<IconCoordinate>()
        val indexToCoordinates = mutableListOf<Pair<Int,Int>>()
        val coordinateToIndex = mutableListOf<MutableList<Int>>()
        var curI = 0
        for (i in 0 until rounds){
            val iconsInRing = ((endAngle - startingAngle) / MathUtils.calculateAngleOnCircle(radius, iconDistance)).toInt()
            val angle = (endAngle - startingAngle)/ iconsInRing
            var col = 0
            var curr = startingAngle
            val currRound = mutableListOf<Int>()
            while (curr < endAngle){
                iconCoordinates.add(IconCoordinate(radius,180 - curr - angle/2))
                indexToCoordinates.add(Pair(row,col))
                currRound.add(curI)
                curI++
                curr += angle
                col++
            }
            coordinateToIndex.add(currRound)
            radius += radiusDiff
            row++
        }
        return IconCoordinatesResult(
            iconCoordinates,
            iconCoordinates.toList().map { c -> MathUtils.getPositionOnCircle(c.distance, c.angle) },
            indexToCoordinates.toList(),
            coordinateToIndex
        )
    }
}