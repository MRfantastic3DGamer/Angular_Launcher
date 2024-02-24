package com.dhruv.angular_launcher.settings_screen.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorLooks
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigationFunctions
import com.dhruv.angular_launcher.apps_data.AppsDataVM
import com.dhruv.angular_launcher.settings_module.prefferences.values.PrefValues
import com.dhruv.angular_launcher.settings_screen.SettingsVM
import com.dhruv.angular_launcher.settings_screen.presentation.components.tabButton
import com.dhruv.angular_launcher.settings_screen.data.SettingsTab
import com.dhruv.angular_launcher.settings_screen.presentation.components.AppNavigation
import com.dhruv.angular_launcher.settings_screen.presentation.components.FluidCursor
import com.dhruv.angular_launcher.settings_screen.presentation.components.GroupsEditor
import com.dhruv.angular_launcher.settings_screen.presentation.components.Slider
import kotlin.jvm.internal.Ref

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SettingsScreen(
    exitSettings: ()->Unit,
) {
    val context = LocalContext.current
    val vm = SettingsVM.getInstance(listOf(
        "sl_width",
        "sl_height",
        "sl_topPadding",
        "sl_downPadding",
        "sl_triggerCurveEdgeCount",
        "sl_selectionCurveOffset",
        "sl_shouldBlur",
        "sl_blurAmount",
        "sl_tint",
        "sl_vibrateOnSelectionChange",
        "sl_vibrationAmount",
        "sl_vibrationTime",

        "fc_fluidCursorLooks",
        "fc_animationSpeed",
        ))

    val apps = vm.appsState.collectAsState().value
    val groups = vm.groupsState.collectAsState().value.toMutableList()

    var selectedTab by remember { mutableStateOf(SettingsTab.Slider) }

    Box(
        modifier = Modifier.fillMaxSize()
    ){

        Row (
            Modifier.fillMaxSize()
        ){
            NavigationRail (
                modifier = Modifier.width(50.dp),
                containerColor = Color.Transparent,
                contentColor = Color.Black,
                windowInsets = WindowInsets(0,0,0,0),
                header = {
                    Icon(imageVector = Icons.Rounded.KeyboardArrowLeft, contentDescription = "back")
                },
            ){
                SettingsTab.entries.forEach {
                    tabButton(text = it.name) {
                        selectedTab = it
                    }
                }
            }

            AnimatedContent(
                targetState = selectedTab,
                label = "settings",
                transitionSpec = {
                    slideIntoContainer(
                        animationSpec = tween(100, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Right
                    ).togetherWith(
                        slideOutOfContainer(
                            animationSpec = tween(100, easing = EaseOut),
                            towards = AnimatedContentTransitionScope.SlideDirection.Right
                        )
                    )
                }
            ) { targetState ->
                when (targetState) {
                    SettingsTab.Theme -> Box (Modifier.fillMaxSize()){

                    }
                    SettingsTab.Slider -> Box (Modifier.fillMaxSize()){
                        Slider(
                            width = vm.values["sl_width"] as MutableState<Float>,
                            height = vm.values["sl_height"] as MutableState<Float>,
                            topPadding = vm.values["sl_topPadding"] as MutableState<Float>,
                            downPadding = vm.values["sl_downPadding"] as MutableState<Float>,
                            triggerCurveEdgeCount = vm.values["sl_triggerCurveEdgeCount"] as MutableState<Int>,
                            selectionCurveOffset = vm.values["sl_selectionCurveOffset"] as MutableState<Float>,
                            shouldBlur = vm.values["sl_shouldBlur"] as MutableState<Boolean>,
                            blurAmount = vm.values["sl_blurAmount"] as MutableState<Float>,
                            tint = vm.values["sl_tint"] as MutableState<Color>,
                            vibrateOnSelectionChange = vm.values["sl_vibrateOnSelectionChange"] as MutableState<Boolean>,
                            vibrationAmount = vm.values["sl_vibrationAmount"] as MutableState<Float>,
                            vibrationTime = vm.values["sl_vibrationTime"] as MutableState<Float>,
                        )
                    }
                    SettingsTab.FluidCursor -> Box (Modifier.fillMaxSize()){
                        FluidCursor(
                            looks = vm.values["fc_looks"] as MutableState<FluidCursorLooks>,
                            animationSpeed = vm.values["fc_animationSpeed"] as MutableState<Float>,
                        )
                    }
                    SettingsTab.AppNavigation -> Box (Modifier.fillMaxSize()){
                        AppNavigation(
                            iconSize = vm.values["an_iconSize"] as MutableState<Float>,
                            enlargeSelectedIconBy = vm.values["an_enlargeSelectedIconBy"] as MutableState<Float>,
                            shouldBlur = vm.values["an_shouldBlur"] as MutableState<Boolean>,
                            blurAmount = vm.values["an_blurAmount"] as MutableState<Float>,
                            tint = vm.values["an_tint"] as MutableState<Color>,
                            option1 = vm.values["an_option1"] as MutableState<RadialAppNavigationFunctions.IconCoordinatesGenerationInput>,
                            option2 = vm.values["an_option2"] as MutableState<RadialAppNavigationFunctions.IconCoordinatesGenerationInput>,
                            option3 = vm.values["an_option3"] as MutableState<RadialAppNavigationFunctions.IconCoordinatesGenerationInput>,
                            option4 = vm.values["an_option4"] as MutableState<RadialAppNavigationFunctions.IconCoordinatesGenerationInput>,
                            option5 = vm.values["an_option5"] as MutableState<RadialAppNavigationFunctions.IconCoordinatesGenerationInput>,
                            vibrateOnSelectionChange = vm.values["an_vibrateOnSelectionChange"] as MutableState<Boolean>,
                            vibrationAmount = vm.values["an_vibrationAmount"] as MutableState<Float>,
                            vibrationTime = vm.values["an_vibrationTime"] as MutableState<Float>,
                        )
                    }
                    SettingsTab.Groups -> Box (Modifier.fillMaxSize()){
                        GroupsEditor(apps, groups) { vm.saveGroup(it) }
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = {
                vm.save(context)
                exitSettings()
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset { IntOffset(-20, -20) },
        ) {
            Text(text = "Save")
        }
    }
}