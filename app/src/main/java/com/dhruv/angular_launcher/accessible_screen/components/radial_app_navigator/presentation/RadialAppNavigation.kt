package com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.presentation

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.round
import com.dhruv.angular_launcher.accessible_screen.components.app_label.data.AppLabelValue
import com.dhruv.angular_launcher.accessible_screen.components.blender.BlendingShader
import com.dhruv.angular_launcher.accessible_screen.components.blender.BlurContainer
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.FluidCursorVM
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.CursorState
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorData
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorValues
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.presentation.FluidCursor
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigationFunctions
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigatorVM
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data.RadialAppNavigatorValues
import com.dhruv.angular_launcher.core.appIcon.AppIcon
import com.dhruv.angular_launcher.core.database.room.AppDatabase
import com.dhruv.angular_launcher.data.enums.SelectionMode
import com.dhruv.angular_launcher.haptics.HapticsHelper
import com.dhruv.angular_launcher.utils.ScreenUtils

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RadialAppNavigation (vm: RadialAppNavigatorVM){

    val context = LocalContext.current
    val DBVM = AppDatabase.getViewModel(context)
    val fluidCursorVM by remember { mutableStateOf(FluidCursorVM()) }

    val runtimeShader = remember {
        RuntimeShader(BlendingShader)
    }

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
    val allOffsets = iconPositionsCompute.offsets

    if (vm.shouldSelectApp){
        vm.prevSelectionIndex = vm.selectionIndex
        vm.selectionIndex = RadialAppNavigationFunctions.getClosest(vm.offsetFromCenter + vm.center, allOffsets, 200f)
        if (vm.selectionIndex != vm.prevSelectionIndex){
            HapticsHelper.appSelectHaptic(context)
            println("haptic")
        }
    }
    else{
        vm.prevSelectionIndex = vm.selectionIndex
        vm.selectionIndex = -1
        vm.selectionAmount = emptyMap()
        if (vm.selectionIndex != vm.prevSelectionIndex){
            HapticsHelper.appSelectHaptic(context)
            println("haptic")
        }
    }

    val selectionOffset: Offset? = allOffsets.getOrNull(vm.selectionIndex)
    val targetCursorPos = selectionOffset?:  if (vm.offsetFromCenter.x >= 0) vm.center else vm.center + vm.offsetFromCenter
    val snap = vm.offsetFromCenter.x > 0f
    val cursorPos by animateOffsetAsState(targetValue = targetCursorPos, label = "cursor-pos", animationSpec = if (snap) spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessLow) else spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMediumLow))

    FluidCursorValues.updateData(
        FluidCursorData(
            targetPosition = cursorPos,
            snap = snap,
            visibility = vm.visibility,
            state = if (snap) CursorState.STUCK_TO_SLIDER else if (vm.selectionIndex == -1) CursorState.FREE else CursorState.STUCK_TO_ICON
        )
    )

    AppLabelValue.updatePackageState( appsPkgsList.getOrElse(vm.selectionIndex, {"@"}) )

    if (vm.visibility){
        vm.selectionAmount = RadialAppNavigationFunctions.getIconSelection(cursorPos, allOffsets, 200f)
        val iconSizeOffset = Offset(vm.iconStyle.size.value, vm.iconStyle.size.value)

        Box(
            modifier = Modifier
                .graphicsLayer {
                    renderEffect = RenderEffect
                        .createShaderEffect(Shader())
                        .asComposeRenderEffect()
                    runtimeShader.setFloatUniform("visibility", 0.2f)
                    renderEffect = RenderEffect
                        .createRuntimeShaderEffect(runtimeShader, "composable")
                        .asComposeRenderEffect()
                }
        ) {
            BlurContainer(
                modifier = Modifier
                    .fillMaxSize(),
                component = {
                    allOffsets.forEachIndexed { index, it ->
                        if (index in appsPkgsList.indices) {
                            Box(
                                modifier = Modifier
                                    .offset { (it - iconSizeOffset).round() }
                                    .size(vm.iconStyle.size)
                                    .clip(CircleShape)
                                    .background(Color.Black)
                            )
                        }
                    }
                    FluidCursor(vm = fluidCursorVM)
                },
                content = {
                    allOffsets.forEachIndexed { index, it ->
                        if (index in appsPkgsList.indices) {
                            val pkgName = appsPkgsList[index]
                            AppIcon(pkgName = pkgName, style = vm.iconStyle, selectionStyle = vm.selectedIconStyle, painter = vm.appsIcons[pkgName], offset = it, selected = vm.selectionAmount[index]?:0f)
                        }
                    }
                }
            )
        }
    }
}