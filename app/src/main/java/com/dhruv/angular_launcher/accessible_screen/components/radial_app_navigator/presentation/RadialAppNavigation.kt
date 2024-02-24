package com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import com.dhruv.angular_launcher.accessible_screen.components.app_label.data.AppLabelValue
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorData
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorValues
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigationFunctions
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigatorVM
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data.RadialAppNavigatorValues
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.presentation.components.AppIcon
import com.dhruv.angular_launcher.apps_data.model.GroupData
import com.dhruv.angular_launcher.data.enums.SelectionMode
import com.dhruv.angular_launcher.utils.ScreenUtils

@Composable
fun RadialAppNavigation (vm: RadialAppNavigatorVM){

    val appsPkgsList: List<String> = when (vm.selectionMode) {
        SelectionMode.NotSelected -> emptyList()
        SelectionMode.ByAlphabet -> vm.appsByChar.collectAsState().value.getOrDefault( vm.sliderSelection , emptyList())
        SelectionMode.BySearch -> emptyList()
        SelectionMode.ByGroup -> (vm.groups.collectAsState().value.firstOrNull{it.name == vm.sliderSelection}?: GroupData()).apps.map { it.packageName }
    }

    val numberOfElements: Int = appsPkgsList.size
    val iconPositionsCompute = RadialAppNavigationFunctions.getUsableOffsets(
        RadialAppNavigatorValues.GetPersistentData.value?.offsetsScales ?: listOf(),
        vm.center,
        numberOfElements,
        vm.sliderPosY,
        ScreenUtils.dpToF(vm.iconStyle.size)*0.5f,
    )
    val offsets = iconPositionsCompute.offsets
    val skips = iconPositionsCompute.skips
    val currentQualityIndex = iconPositionsCompute.qualityIndex
    if (vm.shouldSelectApp){
        val possibleSelections = RadialAppNavigationFunctions.getPossibleIconIndeces(
            vm.offsetFromCenter,
            vm.iconsPerRound[currentQualityIndex],
            vm.roundsStartingDistances[currentQualityIndex],
            skips
        ).toList()

        vm.selectionIndex = RadialAppNavigationFunctions.getBestIndex(
            vm.center + vm.offsetFromCenter,
            offsets,
            possibleSelections
        )
    }
    else{
        vm.selectionIndex = -1
    }

    var selectionOffset: Offset? = null

    if (vm.visibility){
        offsets.forEachIndexed { index, it ->
            if (index == vm.selectionIndex) selectionOffset = it
            val pkgName = appsPkgsList[index]
            AppIcon(pkgName = pkgName, style = vm.iconStyle, painter = vm.appsIcons[pkgName], offset = it, selected = vm.selectionIndex == index)
        }
    }

    FluidCursorValues.updateData(FluidCursorData(
        targetPosition = selectionOffset?:  if (vm.offsetFromCenter.x >= 0) vm.center else vm.center + vm.offsetFromCenter,
        snap = vm.offsetFromCenter.x > 0f,
        visibility = vm.visibility
    ))

    AppLabelValue.updatePackageState( appsPkgsList.getOrElse(vm.selectionIndex, {"@"}) )
}