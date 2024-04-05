package com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.presentation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.accessible_screen.components.app_label.data.AppLabelValue
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorValues
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigationFunctions
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigatorVM
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data.RadialAppNavigatorValues
import com.dhruv.angular_launcher.core.appIcon.AppIcon
import com.dhruv.angular_launcher.core.database.room.AppDatabase
import com.dhruv.angular_launcher.core.database.room.ThemeDatabase
import com.dhruv.angular_launcher.core.database.room.models.getIconStyles
import com.dhruv.angular_launcher.core.resources.AllResources
import com.dhruv.angular_launcher.data.enums.SelectionMode
import com.dhruv.angular_launcher.haptics.HapticsHelper
import com.dhruv.angular_launcher.utils.MathUtils.distance
import com.dhruv.angular_launcher.utils.ScreenUtils

@Composable
fun RadialAppNavigation (vm: RadialAppNavigatorVM){

    val context = LocalContext.current
    val DBVM = AppDatabase.getViewModel(context)
    val themeVM = remember { ThemeDatabase.getViewModel(context) }
    val state = themeVM.currTheme
    val iconStyles = state.getIconStyles()

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
//    println("all offsets: ${RadialAppNavigatorValues.GetPersistentData.value?.offsetsScales?.map { it.size } ?: -1}")
    val iconPositionsCompute = RadialAppNavigationFunctions.getUsableOffsets(
        RadialAppNavigatorValues.GetPersistentData.value?.offsetsScales ?: listOf(),
        vm.center,
        numberOfElements,
        vm.sliderPosY,
        ScreenUtils.dpToF( iconStyles[0].size)*0.5f,
        vm.sliderHeight,
        themeVM.currTheme.sliderWidth.dp
    )

    val usedOffsets = iconPositionsCompute.offsets
    if (usedOffsets.size > 0) {
        themeVM.addData(
            AllResources.GroupZoneStartRadios.name,
            distance(vm.center, usedOffsets.first())
        )
        themeVM.addData(
            AllResources.GroupZoneEndRadios.name,
            distance(vm.center, usedOffsets.last())
        )
    }
    if (vm.shouldSelectApp){
        // region selecting app
        vm.prevSelectionIndex = vm.selectionIndex
        vm.selectionIndex = RadialAppNavigationFunctions.getClosest(vm.offsetFromCenter + vm.center, usedOffsets, 200f)
        if (vm.selectionIndex != vm.prevSelectionIndex){
            themeVM.addData(AllResources.SelectedIconIndex.name, vm.selectionIndex)
            HapticsHelper.appSelectHaptic(context)
        }
        // endregion
    }
    else{
        // region reset app selection
        vm.prevSelectionIndex = vm.selectionIndex
        vm.selectionIndex = -1
        vm.selectionAmount = emptyMap()
        if (vm.selectionIndex != vm.prevSelectionIndex){
            HapticsHelper.appSelectHaptic(context)
        }
        // endregion
    }

    val selectionOffset: Offset? = usedOffsets.getOrNull(vm.selectionIndex)
    val targetCursorPos = selectionOffset?:  if (vm.offsetFromCenter.x >= 0) vm.center else vm.center + vm.offsetFromCenter
    val snap = vm.offsetFromCenter.x > 0f
    val cursorPos by animateOffsetAsState(targetValue = targetCursorPos, label = "cursor-pos", animationSpec = if (snap) spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessLow) else spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessLow))

    FluidCursorValues.updatesFromAppsNavigator(selectionOffset)

    AppLabelValue.updatePackageState( appsPkgsList.getOrElse(vm.selectionIndex, {"@"}) )

//    println("slider selection: ${vm.sliderSelection}, icons cnt: ${usedOffsets.size}, offsetes: ${usedOffsets}")

    if (vm.visibility){
        vm.selectionAmount = RadialAppNavigationFunctions.getIconSelection(cursorPos, usedOffsets, 200f)

        // update in shader
        themeVM.addData(AllResources.IconsCount.name, usedOffsets.size)
        themeVM.addData(AllResources.IconsPositions.name, usedOffsets.map { ScreenUtils.ShaderOffset(it) })

        usedOffsets.forEachIndexed { index, it ->
            if (index in appsPkgsList.indices) {
                val pkgName = appsPkgsList[index]
                AppIcon(
                    pkgName = pkgName,
                    style = iconStyles[0],
                    selectionStyle = iconStyles[1],
                    painter = vm.appsIcons[pkgName],
                    offset = it,
                    selected = vm.selectionAmount[index] ?: 0f
                )
            }
        }
    }
    else{
        // update in shader
        themeVM.addData(AllResources.IconsCount.name, 0)
        themeVM.addData(AllResources.IconsPositions.name, emptyList<Offset>())
    }
}