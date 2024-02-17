package com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data.IconCoordinate
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data.RadialAppNavigatorData
import com.dhruv.angular_launcher.accessible_screen.components.slider.data.SliderValues
import com.dhruv.angular_launcher.utils.MathUtils
import com.dhruv.angular_launcher.utils.ScreenUtils



object RadialAppNavigationFunctions {
    fun getRadialAppNavigatorData (
        selectionPaddingX: Float,
        selectionPosY: Float,
        sliderWidth: Float,
        selection_i: Int,
        sliderPosY: Float,
        touchPos: Offset,
    ): RadialAppNavigatorData {
        val center = Offset(ScreenUtils.fromRight(sliderWidth+ selectionPaddingX), selectionPosY)
        return RadialAppNavigatorData(
            currentSelectionIndex = selection_i,
            center = center,
            sliderPositionY = sliderPosY,
            offsetFromCenter = touchPos - center
        )
    }

    fun getPositionOnCircle(coordinate: IconCoordinate): Offset {
        return MathUtils.getPositionOnCircle(coordinate.distance, -coordinate.angle)
    }

    @Stable
    data class IconOffsetComputeResult(
        val qualityIndex: Int,
        val offsets: List<Offset>,
        val skips: List<Pair<Int, Int>>,
    )

    fun getUsableOffsets(
        list: List<List<Offset>>,
        center: Offset,
        count: Int,
        sliderPosY: Float
    ): IconOffsetComputeResult {
        val sliderHeight = ScreenUtils.dpToF(SliderValues.GetPersistentData.value!!.height)
        val top = sliderPosY + 50f
        val bot = sliderPosY + sliderHeight - 50f
        val left = 100f
        val iconSizeOffset = Offset(2.5f, 2.5f)

        fun getResult(iconSizePreset: Int): IconOffsetComputeResult {
            val offsets = mutableListOf<Offset>()
            val skips= mutableListOf<Pair<Int,Int>>()
            var j = 0
            for (i in 0 until count) {
                if (j !in list[iconSizePreset].indices) {
                    if (iconSizePreset >= list.size - 1)
                        return IconOffsetComputeResult(iconSizePreset, offsets, skips)
                    return getResult(iconSizePreset + 1)
                }
                var c = center + list[iconSizePreset][j] - iconSizeOffset
                if (!((c.x > left) && (c.y > top) && (c.y < bot))){
                    val skipStart: Int = j
                    while (!((c.x > left) && (c.y > top) && (c.y < bot))) {
                        j++
                        if (j !in list[iconSizePreset].indices) {
                            if (iconSizePreset >= list.size - 1)
                                return IconOffsetComputeResult(iconSizePreset, offsets, skips)
                            return getResult(iconSizePreset + 1)
                        }
                        c = center + list[iconSizePreset][j] - iconSizeOffset
                    }
                    val skipEnd: Int = j
                    skips.add(Pair(skipStart,skipEnd))
                }
                j++
                offsets.add(c)
            }
            return IconOffsetComputeResult(iconSizePreset, offsets, skips)
        }
        return getResult(0)
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
        val coordinateToIndex: List<List<Int>>,
        val iconsPerRound: List<Int>,
        val startingPointOfRound: List<Float>,
    )
    fun generateIconCoordinates (input: IconCoordinatesGenerationInput): IconCoordinatesResult {
        val startingRadius = input.startingRadius
        val radiusDiff = input.radiusDiff
        val iconDistance = input.iconDistance
        val rounds = input.rounds
        val startingAngle:Double = -90.0
        val endAngle = 90.0

        val indexToCoordinates = mutableListOf<Pair<Int,Int>>()
        val coordinateToIndex = mutableListOf<MutableList<Int>>()
        val iconsPerRound = mutableListOf<Int>()
        val startingPointOfRound = mutableListOf<Float>()

        var row = 0
        var radius = startingRadius
        val iconCoordinates = mutableListOf<IconCoordinate>()
        var curI = 0
        for (i in 0 until rounds){
            startingPointOfRound.add((radius.toFloat() - input.radiusDiff/2).toFloat())
            val iconsInRing = ((endAngle - startingAngle) / MathUtils.calculateAngleOnCircle(radius, iconDistance)).toInt()
            iconsPerRound.add(iconsInRing)
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
            coordinateToIndex,
            iconsPerRound,
            startingPointOfRound,
        )
    }

    fun getSelectedIconIndex (touchOffset: Offset, iconsPerRound: List<Int>, distancePerRound: List<Float>, skips: List<Pair<Int, Int>>): Int {
        val dist  = MathUtils.calculateDistance(Offset.Zero, touchOffset)
        val angle = MathUtils.calculateAngle(Offset(0f,10f), Offset.Zero, touchOffset)

        var curRound = 0
        var curIndex = 0

        if (dist < distancePerRound[0]) {
            println("skipped")
            return -1
        }
        while (curRound+1 < iconsPerRound.size && dist > distancePerRound[curRound+1]) curIndex+= iconsPerRound[curRound++]

        val anglePerIndex = 180f/iconsPerRound[curRound]
        var curAngle = -anglePerIndex/2
        while (curAngle < angle){
            curIndex++
            curAngle+= anglePerIndex
        }

        var curSkipPair = 0
        var skippedIndeces = 0

        while (curSkipPair<skips.size && skips[curSkipPair].second < curIndex){
            skippedIndeces += skips[curSkipPair].second - skips[curSkipPair].first
            curSkipPair++
        }

        if (curSkipPair<skips.size && curIndex > skips[curSkipPair].first && curIndex < skips[curSkipPair].second) {
            println("skipped")
            return -1
        }

        return curIndex - skippedIndeces
    }
}