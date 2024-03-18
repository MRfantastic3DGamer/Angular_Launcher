package com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.presentation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import com.dhruv.angular_launcher.accessible_screen.components.app_label.data.AppLabelValue
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.CursorState
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorData
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorValues
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigationFunctions
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigatorVM
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data.RadialAppNavigatorValues
import com.dhruv.angular_launcher.core.appIcon.AppIcon
import com.dhruv.angular_launcher.core.database.room.AppDatabase
import com.dhruv.angular_launcher.data.enums.SelectionMode
import com.dhruv.angular_launcher.haptics.HapticsHelper
import com.dhruv.angular_launcher.utils.ScreenUtils

@Composable
fun RadialAppNavigation (vm: RadialAppNavigatorVM){

    val context = LocalContext.current
    val DBVM = AppDatabase.getViewModel(context)

    val appsPerGroup = DBVM.groups.collectAsState(initial = emptyList()).value.map {
        it._id.toString() to DBVM.getVisibleAppsForGroup(it._id).collectAsState(initial = emptyList()).value.map { it.packageName }
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
    val allOffsets = iconPositionsCompute.offsets

    if (vm.shouldSelectApp){
        vm.prevSelectionIndex = vm.selectionIndex
        vm.selectionIndex = RadialAppNavigationFunctions.getClosest(vm.offsetFromCenter + vm.center, allOffsets, 200f)
        if (vm.selectionIndex != vm.prevSelectionIndex){
            HapticsHelper.appSelectHaptic(context)
        }
    }
    else{
        vm.prevSelectionIndex = vm.selectionIndex
        vm.selectionIndex = -1
        vm.selectionAmount = emptyMap()
        if (vm.selectionIndex != vm.prevSelectionIndex){
            HapticsHelper.appSelectHaptic(context)
        }
    }

    val selectionOffset: Offset? = allOffsets.getOrNull(vm.selectionIndex)
    val targetCursorPos = selectionOffset?:  if (vm.offsetFromCenter.x >= 0) vm.center else vm.center + vm.offsetFromCenter
    val snap = vm.offsetFromCenter.x > 0f
    val cursorPos by animateOffsetAsState(targetValue = targetCursorPos, label = "cursor-pos", animationSpec = if (snap) spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessLow) else spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessLow))

    FluidCursorValues.updateData(
        FluidCursorData(
            targetPosition = cursorPos,
            snap = snap,
            visibility = vm.visibility,
            state = if (snap) CursorState.STUCK_TO_SLIDER else if (vm.selectionIndex == -1) CursorState.FREE else CursorState.STUCK_TO_ICON
        )
    )

    AppLabelValue.updatePackageState( appsPkgsList.getOrElse(vm.selectionIndex, {"@"}) )

    vm.mousePosToShader(cursorPos.x, ScreenUtils.fromDown(cursorPos.y))

    val iconSizeOffset = Offset(vm.iconStyle.size.value, vm.iconStyle.size.value)
    if (vm.visibility){
        vm.selectionAmount = RadialAppNavigationFunctions.getIconSelection(cursorPos, allOffsets, 200f)

        vm.iconPositionsToShader(allOffsets.map { it.copy(y = ScreenUtils.fromDown(it.y)) })
        allOffsets.forEachIndexed { index, it ->
            if (index in appsPkgsList.indices) {
                val pkgName = appsPkgsList[index]
                AppIcon(
                    pkgName = pkgName,
                    style = vm.iconStyle,
                    selectionStyle = vm.selectedIconStyle,
                    painter = vm.appsIcons[pkgName],
                    offset = it,
                    selected = vm.selectionAmount[index] ?: 0f
                )
            }
        }

//        FluidCursor(vm = fluidCursorVM)
    }
    else{
        vm.iconPositionsToShader(emptyList())
    }
}