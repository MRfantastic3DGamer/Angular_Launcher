package com.dhruv.angular_launcher.settings_screen.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
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
import androidx.compose.runtime.derivedStateOf
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
import com.dhruv.angular_launcher.accessible_screen.data.VibrationData
import com.dhruv.angular_launcher.data.models.IconCoordinatesGenerationInput
import com.dhruv.angular_launcher.data.models.IconStyle
import com.dhruv.angular_launcher.database.room.AppDatabase
import com.dhruv.angular_launcher.settings_screen.SettingsVM
import com.dhruv.angular_launcher.settings_screen.data.SettingsTab
import com.dhruv.angular_launcher.settings_screen.presentation.components.AppNavigation
import com.dhruv.angular_launcher.settings_screen.presentation.components.FluidCursor
import com.dhruv.angular_launcher.settings_screen.presentation.components.Slider
import com.dhruv.angular_launcher.settings_screen.presentation.components.apps.AppsEditing
import com.dhruv.angular_launcher.settings_screen.presentation.components.apps.AppsEditingVM
import com.dhruv.angular_launcher.settings_screen.presentation.components.groups.GroupsEditingVM
import com.dhruv.angular_launcher.settings_screen.presentation.components.groups.GroupsEditor
import com.dhruv.angular_launcher.settings_screen.presentation.components.tabButton

@Composable
fun SettingsScreen(
    vm: SettingsVM,
    exitSettings: ()->Unit,
) {
    val context = LocalContext.current
    val DBVM = remember { AppDatabase.getViewModel(context) }
    val apps = DBVM.apps.collectAsState(initial = emptyList()).value

    val groupsEditingVM = remember { GroupsEditingVM() }

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
                            width = vm.tryToGetState("sl_width") as MutableState<Float>,
                            height = vm.tryToGetState("sl_height") as MutableState<Float>,
                            topPadding = vm.tryToGetState("sl_topPadding") as MutableState<Float>,
                            downPadding = vm.tryToGetState("sl_downPadding") as MutableState<Float>,
                            triggerCurveEdgeCount = vm.tryToGetState("sl_triggerCurveEdgeCount") as MutableState<Int>,
                            selectionCurveOffset = vm.tryToGetState("sl_selectionCurveOffset") as MutableState<Float>,
                            shouldBlur = vm.tryToGetState("sl_shouldBlur") as MutableState<Boolean>,
                            blurAmount = vm.tryToGetState("sl_blurAmount") as MutableState<Float>,
                            tint = vm.tryToGetState("sl_tint") as MutableState<Color>,
                            vibration = vm.tryToGetState("sl_vibration") as MutableState<VibrationData>,
                        )
                    }
                    SettingsTab.FluidCursor -> Box (Modifier.fillMaxSize()){
                        FluidCursor(
                            looks = vm.tryToGetState("fc_fluidCursorLooks") as MutableState<FluidCursorLooks>,
                            animationSpeed = vm.tryToGetState("fc_animationSpeed") as MutableState<Float>,
                        )
                    }
                    SettingsTab.AppNavigation -> Box (Modifier.fillMaxSize()){

                        AppNavigation(
                            iconStyle = vm.tryToGetState("an_iconStyle") as MutableState<IconStyle>,
                            enlargeSelectedIconBy = vm.tryToGetState("an_enlargeSelectedIconBy") as MutableState<Float>,
                            shouldBlur = vm.tryToGetState("an_shouldBlur") as MutableState<Boolean>,
                            blurAmount = vm.tryToGetState("an_blurAmount") as MutableState<Float>,
                            tint = vm.tryToGetState("an_tint") as MutableState<Color>,
                            option1 = vm.tryToGetState("an_option1") as MutableState<IconCoordinatesGenerationInput>,
                            option2 = vm.tryToGetState("an_option2") as MutableState<IconCoordinatesGenerationInput>,
                            option3 = vm.tryToGetState("an_option3") as MutableState<IconCoordinatesGenerationInput>,
                            option4 = vm.tryToGetState("an_option4") as MutableState<IconCoordinatesGenerationInput>,
                            option5 = vm.tryToGetState("an_option5") as MutableState<IconCoordinatesGenerationInput>,
                            vibrationData = vm.tryToGetState("an_vibration") as MutableState<VibrationData>,
                        )
                    }
                    SettingsTab.Apps -> Box(Modifier.fillMaxSize()){
                        val appsEditingVM = remember(apps) {
                            derivedStateOf{
                                AppsEditingVM(apps, {DBVM.updateApp(it)})
                            }
                        }
                        AppsEditing(vm = appsEditingVM.value)
                    }
                    SettingsTab.Groups -> Box (Modifier.fillMaxSize()){
                        GroupsEditor(groupsEditingVM)
                    }
                }
            }
        }
        if (selectedTab != SettingsTab.Apps && selectedTab != SettingsTab.Groups) {
            FloatingActionButton(
                onClick = {
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
}