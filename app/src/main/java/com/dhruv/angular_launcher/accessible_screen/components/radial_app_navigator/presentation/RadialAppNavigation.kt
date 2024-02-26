package com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import com.dhruv.angular_launcher.accessible_screen.components.app_label.data.AppLabelValue
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorData
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorValues
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigationFunctions
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigatorVM
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data.RadialAppNavigatorValues
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.presentation.components.AppIcon
import com.dhruv.angular_launcher.data.enums.SelectionMode
import com.dhruv.angular_launcher.database.room.AppDatabase
import com.dhruv.angular_launcher.utils.ScreenUtils

@Composable
fun RadialAppNavigation (vm: RadialAppNavigatorVM){

    val context = LocalContext.current
    val DBVM = remember() { AppDatabase.getViewModel(context) }

    val appsPerGroup = DBVM.groups.collectAsState(initial = emptyList()).value.map {
        it._id.toString() to DBVM.getAppsForGroup(it._id).collectAsState(initial = emptyList()).value.map { it.packageName }
    }.toMap()

    val appsPkgsList: List<String> = when (vm.selectionMode) {
        SelectionMode.NotSelected -> emptyList()
        SelectionMode.ByAlphabet -> DBVM.appsByChar.collectAsState(initial = emptyMap()).value.getOrDefault(vm.sliderSelection, emptyList()).map { it.packageName }
        SelectionMode.BySearch -> emptyList()
        SelectionMode.ByGroup -> appsPerGroup.getOrDefault(vm.sliderSelection, emptyList())
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