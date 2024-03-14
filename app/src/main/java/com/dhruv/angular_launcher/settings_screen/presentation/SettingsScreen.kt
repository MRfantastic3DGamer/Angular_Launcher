package com.dhruv.angular_launcher.settings_screen.presentation

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorLooks
import com.dhruv.angular_launcher.accessible_screen.data.VibrationData
import com.dhruv.angular_launcher.core.appIcon.IconStyle
import com.dhruv.angular_launcher.core.database.room.AppDatabase
import com.dhruv.angular_launcher.data.models.IconCoordinatesGenerationInput
import com.dhruv.angular_launcher.settings_screen.SettingsVM
import com.dhruv.angular_launcher.settings_screen.data.SettingsTab
import com.dhruv.angular_launcher.settings_screen.presentation.components.app_navigator.AppNavigation
import com.dhruv.angular_launcher.settings_screen.presentation.components.apps.AppsEditing
import com.dhruv.angular_launcher.settings_screen.presentation.components.apps.AppsEditingVM
import com.dhruv.angular_launcher.settings_screen.presentation.components.cursor.FluidCursor
import com.dhruv.angular_launcher.settings_screen.presentation.components.groups.GroupsEditingVM
import com.dhruv.angular_launcher.settings_screen.presentation.components.groups.GroupsEditor
import com.dhruv.angular_launcher.settings_screen.presentation.components.slider.Slider
import com.dhruv.angular_launcher.settings_screen.presentation.components.tabButton
import com.dhruv.angular_launcher.settings_screen.presentation.components.theme.Theme

@Composable
fun SettingsScreen(
    vm: SettingsVM,
    exitSettings: (Context)->Unit,
) {
    val context = LocalContext.current
    val DBVM = AppDatabase.getViewModel(context)
    val apps = DBVM.apps.collectAsState(initial = emptyList())

    var selectedTab by remember { mutableStateOf(SettingsTab.Theme) }

    Box(
        modifier = Modifier.fillMaxSize()
    ){

        Row (
            Modifier.fillMaxSize()
        ){
            NavigationRail (
                modifier = Modifier
                    .width(50.dp)
                    .background(Color.Black),
                containerColor = Color.Transparent,
                contentColor = Color.Black,
                windowInsets = WindowInsets(0,0,0,0),
                header = {
                    Icon(imageVector = Icons.Rounded.KeyboardArrowLeft, contentDescription = "back")
                },
            ){
                tabButton(text = SettingsTab.Theme.name, selectedTab == SettingsTab.Theme) {
                    selectedTab = SettingsTab.Theme
                }
                tabButton(text = SettingsTab.Apps.name, selectedTab == SettingsTab.Apps) {
                    selectedTab = SettingsTab.Apps
                }
                tabButton(text = SettingsTab.Groups.name, selectedTab == SettingsTab.Groups) {
                    selectedTab = SettingsTab.Groups
                }
                tabButton(text = SettingsTab.Slider.name, selectedTab == SettingsTab.Slider) {
                    selectedTab = SettingsTab.Slider
                }
                tabButton(text = SettingsTab.AppNavigation.name, selectedTab == SettingsTab.AppNavigation) {
                    selectedTab = SettingsTab.AppNavigation
                }
            }

            Box (
                Modifier
                    .background(Color.Black.copy(alpha = 0.7f))
            ) {

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
                            Theme()
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
                                selectedIconStyle = vm.tryToGetState("an_selectedIconStyle") as MutableState<IconStyle>,
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
                            val appsEditingVM = remember(apps) { derivedStateOf{ AppsEditingVM(apps.value, DBVM::updateApp) } }
                            AppsEditing(vm = appsEditingVM.value)
                        }
                        SettingsTab.Groups -> Box (Modifier.fillMaxSize()){
                            val groupsEditingVM = remember { GroupsEditingVM() }
                            GroupsEditor(groupsEditingVM)
                        }
                    }
                }
            }
        }
        Button(
            onClick = {
                exitSettings(context)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd),
        ) {
            Text(text = "Save")
        }
    }
}