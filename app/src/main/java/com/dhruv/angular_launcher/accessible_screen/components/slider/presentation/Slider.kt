package com.dhruv.angular_launcher.accessible_screen.components.slider.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.accessible_screen.components.app_label.AppLabelFunctions
import com.dhruv.angular_launcher.accessible_screen.components.app_label.data.AppLabelValue
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorValues
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigationFunctions.getRadialAppNavigatorData
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data.RadialAppNavigatorValues
import com.dhruv.angular_launcher.accessible_screen.components.slider.SliderFunctions
import com.dhruv.angular_launcher.accessible_screen.components.slider.SliderVM
import com.dhruv.angular_launcher.accessible_screen.components.slider.presentation.components.AllChoices
import com.dhruv.angular_launcher.core.database.room.AppDatabase
import com.dhruv.angular_launcher.core.database.room.ThemeDatabase
import com.dhruv.angular_launcher.core.database.room.models.getSliderHeight
import com.dhruv.angular_launcher.data.enums.SelectionMode
import com.dhruv.angular_launcher.haptics.HapticsHelper
import com.dhruv.angular_launcher.utils.ScreenUtils

@Composable
fun Slider (
    vm: SliderVM,
){
    val context = LocalContext.current
    val DBVM = AppDatabase.getViewModel(context)
    val theme = ThemeDatabase.getViewModel(context).currTheme


    val allOptions: List<String> = when (vm.selectionMode) {
        SelectionMode.NotSelected -> emptyList()
        SelectionMode.ByAlphabet -> DBVM.appsStartingChars.collectAsState(initial = emptyList()).value
        SelectionMode.BySearch -> emptyList()
        SelectionMode.ByGroup -> DBVM.groups.collectAsState(initial = emptyList()).value.map { it.iconKey }
    }

    val height = theme.getSliderHeight(allOptions.size)


    if (vm.UpdateTrigger()){
        val posY = SliderFunctions.GetSliderPositionY(vm.touchPos.y, height, vm.sliderPos.y)
        vm.sliderPos = SliderFunctions.calculateSliderPosition(posY, theme.sliderWidth)

        val appLabelPosition = AppLabelFunctions.calculatePosition(300f, vm.sliderPos.y, height, 20f)
        AppLabelValue.updatePos(appLabelPosition)
    }


    LaunchedEffect(key1 = vm.touchPos){
        if (vm.shouldUpdateSelection) {
            val selection = SliderFunctions.calculateCurrentSelection(
                allOptions.size,
                height,
                vm.touchPos.y - vm.sliderPos.y
            )
            vm.prevSelectionIndex = vm.selectionIndex
            vm.selectionIndex = selection.index
            vm.selectionPosY = selection.posY
            if (vm.selectionIndex != vm.prevSelectionIndex){
                HapticsHelper.groupSelectHaptic(context)
            }
            FluidCursorValues.updatesFromSlider(
                vm.sliderPos + Offset(0f, selection.posY),
                vm.touchPos.y - vm.sliderPos.y < 0f
            )
        }
    }

//    println("slider selection idx: ${vm.selectionIndex}")

    val currentSelectionPosition: Float by animateFloatAsState(targetValue = vm.selectionPosY, label = "selection_pos_Y")
    val currentFuzzySelection by animateFloatAsState(targetValue = vm.selectionIndex.toFloat(), label = "currentSelection")
    val currentOffset by animateOffsetAsState(targetValue = vm.sliderPos, label = "trigger_offset")
//    val path = SliderFunctions.SliderPath(currentOffset, SliderValues.GetPersistentData.value!!, currentSelectionPosition)


    RadialAppNavigatorValues.updateData(
        getRadialAppNavigatorData(
            selection = when (vm.selectionMode) {
                SelectionMode.NotSelected -> "@"
                SelectionMode.ByAlphabet -> DBVM.appsStartingChars.collectAsState(initial = emptyList()).value.getOrElse(vm.selectionIndex, {"@"})
                SelectionMode.BySearch -> "@"
                SelectionMode.ByGroup -> DBVM.groupIds.collectAsState(initial = emptyList()).value.getOrElse(vm.selectionIndex, {"@"}).toString()
            },
            sliderWidth = theme.sliderWidth,
            sliderHeight = height,
            selectionPaddingX = theme.sidePadding,
            selectionPosY = currentSelectionPosition + currentOffset.y,
            sliderPosY = vm.sliderPos.y,
            touchPos = vm.touchPos,
            visibility = vm.visible,
            selectionMode = vm.selectionMode,
        )
    )

    // open settings if user is trying to use groups but does`nt have em
    val groupsAvailable = allOptions.isNotEmpty()
    if (!groupsAvailable && vm.visible){
        vm.moveToSettingsValue += 0.01f
        if (vm.moveToSettingsValue > 2.5){
            vm.moveToSettingsValue = 0f
            vm.goToEditGroups(context)
        }
    }
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
//        GlassOverWallpaper(
//            Modifier.fillMaxSize(),
//            path = path,
//            blur = 10.dp
//        )
        AllChoices(
            offset = currentOffset,
            height = height,
            width = ScreenUtils.fToDp(theme.sliderWidth - 20),
            allOptions = allOptions,
            currentSelection = currentFuzzySelection,
            shift = ScreenUtils.dpToF(theme.sidePadding.dp),
            selectionMode = vm.selectionMode,
        )
    }
}