package com.dhruv.angular_launcher.accessible_screen.components.slider.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigationFunctions.getRadialAppNavigatorData
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data.RadialAppNavigatorValues
import com.dhruv.angular_launcher.accessible_screen.components.slider.SliderFunctions
import com.dhruv.angular_launcher.accessible_screen.components.slider.SliderVM
import com.dhruv.angular_launcher.accessible_screen.components.slider.data.SliderValues
import com.dhruv.angular_launcher.accessible_screen.components.slider.presentation.components.AllChoices
import com.dhruv.angular_launcher.core.database.prefferences.values.PrefValues
import com.dhruv.angular_launcher.core.database.room.AppDatabase
import com.dhruv.angular_launcher.core.wallpaper.GlassOverWallpaper
import com.dhruv.angular_launcher.data.enums.SelectionMode
import com.dhruv.angular_launcher.debug.DebugLayerValues
import com.dhruv.angular_launcher.utils.ScreenUtils

@Composable
fun Slider (
    vm: SliderVM,
){
    val context = LocalContext.current
    val DBVM = remember() { AppDatabase.getViewModel(context) }

    val allOptions: List<String> = when (vm.selectionMode) {
        SelectionMode.NotSelected -> emptyList()
        SelectionMode.ByAlphabet -> DBVM.appsStartingChars.collectAsState(initial = emptyList()).value
        SelectionMode.BySearch -> emptyList()
        SelectionMode.ByGroup -> DBVM.groups.collectAsState(initial = emptyList()).value.map { it.iconKey }
    }

    LaunchedEffect(key1 = vm.touchPos){
        DebugLayerValues.addString((allOptions.size-1).toString(), "el_cnt")
        DebugLayerValues.addString((vm.height).toString(), "sl_h")
        DebugLayerValues.addString((vm.touchPos.y - vm.sliderPos.y).toString(), "f-pos")
        if (vm.shouldUpdateSelection) {
            val selection = SliderFunctions.calculateCurrentSelection(
                allOptions.size,
                vm.height,
                vm.touchPos.y - vm.sliderPos.y
            )
            vm.selectionIndex = selection.index
            vm.selectionPosY = selection.posY
            DebugLayerValues.addString((selection.index).toString(), "sl_select")
        }
    }

    val currentSelectionPosition: Float by animateFloatAsState(targetValue = vm.selectionPosY, label = "selection_pos_Y")
    val currentFuzzySelection by animateFloatAsState(targetValue = vm.selectionIndex.toFloat(), label = "currentSelection")
    val currentOffset by animateOffsetAsState(targetValue = vm.sliderPos, label = "trigger_offset")
    val path = SliderFunctions.SliderPath(currentOffset, SliderValues.GetPersistentData.value!!, currentSelectionPosition)

    val groupsAvailable by remember { derivedStateOf { vm.selectionIndex != -1 } }

    RadialAppNavigatorValues.updateData(
        getRadialAppNavigatorData(
            selection = when (vm.selectionMode) {
                SelectionMode.NotSelected -> "@"
                SelectionMode.ByAlphabet -> DBVM.appsStartingChars.collectAsState(initial = emptyList()).value.getOrElse(vm.selectionIndex, {"@"})
                SelectionMode.BySearch -> "@"
                SelectionMode.ByGroup -> DBVM.groupIds.collectAsState(initial = emptyList()).value.getOrElse(vm.selectionIndex, {"@"}).toString()
            },
            sliderWidth = ScreenUtils.dpToF(SliderValues.GetPersistentData.value?.width ?: 50.dp),
            selectionPaddingX = PrefValues.sl_selectionCurveOffset,
            selectionPosY = currentSelectionPosition + currentOffset.y,
            sliderPosY = vm.sliderPos.y,
            touchPos = vm.touchPos,
            visibility = vm.visible,
            selectionMode = vm.selectionMode,
        )
    )

    AnimatedVisibility(
        visible = !groupsAvailable && vm.visible,
        Modifier,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Snackbar (
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
            ,
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(text = "please add a group from settings")
        }
    }
    AnimatedVisibility(
        visible = groupsAvailable && vm.visible,
        Modifier,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        GlassOverWallpaper(
            Modifier.fillMaxSize(),
            path = path,
            blur = 10.dp
        )
        AllChoices(
            offset = currentOffset,
            height = vm.height,
            width = ScreenUtils.fToDp(vm.width - 20),
            allOptions = allOptions,
            currentSelection = currentFuzzySelection,
            shift = vm.sidePadding,
            selectionMode = vm.selectionMode,
        )
    }
}