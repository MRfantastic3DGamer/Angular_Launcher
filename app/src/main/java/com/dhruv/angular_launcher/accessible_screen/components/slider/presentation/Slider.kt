package com.dhruv.angular_launcher.accessible_screen.components.slider.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.core.math.MathUtils.clamp
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigationFunctions.getRadialAppNavigatorData
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data.RadialAppNavigatorValues
import com.dhruv.angular_launcher.accessible_screen.components.slider.SliderFunctions
import com.dhruv.angular_launcher.accessible_screen.components.slider.SliderVM
import com.dhruv.angular_launcher.accessible_screen.components.slider.data.SliderValues
import com.dhruv.angular_launcher.accessible_screen.components.slider.presentation.components.AllChoices
import com.dhruv.angular_launcher.accessible_screen.components.slider.presentation.components.SliderShape
import com.dhruv.angular_launcher.settings_module.prefferences.values.PrefValues
import com.dhruv.angular_launcher.utils.ScreenUtils

@Composable
fun Slider (
    vm: SliderVM,
){
    if (vm.visible) {
        val currentSelectionPosition: Float by animateFloatAsState(targetValue = vm.selectionPosY, label = "selection_pos_Y")
        val currentFuzzySelection by animateFloatAsState(targetValue = vm.selectionIndex.toFloat(), label = "currentSelection")
        val currentOffset by animateOffsetAsState(targetValue = vm.sliderPos, label = "trigger_offset")
        val path = SliderFunctions.SliderPath(currentOffset, SliderValues.GetPersistentData.value!!, currentSelectionPosition)

        RadialAppNavigatorValues.updateData(
            getRadialAppNavigatorData(
                selection_i = clamp(vm.selectionIndex, 0, 25),
                sliderWidth = ScreenUtils.dpToF(SliderValues.GetPersistentData.value?.width ?: 50.dp),
                selectionPaddingX = PrefValues.sl_selectionCurveOffset,
                selectionPosY = currentSelectionPosition + currentOffset.y,
                sliderPosY = vm.sliderPos.y,
                touchPos = vm.touchPos
            )
        )

        SliderShape(path)
        AllChoices(
            offset = currentOffset,
            height = vm.height,
            elementsCount = vm.elementsCount,
            currentSelection = currentFuzzySelection,
            shift = vm.sidePadding
        )
    }
}