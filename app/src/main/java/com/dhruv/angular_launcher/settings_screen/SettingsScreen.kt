package com.dhruv.angular_launcher.settings_screen

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
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.dhruv.angular_launcher.settings_module.SettingsColumn
import com.dhruv.angular_launcher.settings_module.prefferences.values.PrefValues
import com.dhruv.angular_launcher.settings_screen.components.SettingsArt
import com.dhruv.angular_launcher.settings_screen.components.tabButton
import com.dhruv.angular_launcher.settings_screen.data.SettingsTab

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(SettingsTab.Theme) }

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
                tabButton(text = "Theme"){
                    selectedTab = SettingsTab.Theme
                }
                tabButton(text = "Slider") {
                    selectedTab = SettingsTab.Slider
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
                        SettingsColumn(
                            map = mapOf(
                                "sl_firstCut" to Float::class.java,
                                "sl_secondCut" to Float::class.java,
                            ),
                            constraints = mapOf(),
                            drawing = SettingsArt.EntryForType,
                            specialDrawing = mapOf(),
                            saveValues = PrefValues.changedValuesMap
                        )
                    }
                    SettingsTab.Slider -> Box (Modifier.fillMaxSize()){
                        SettingsColumn(
                            map = mapOf(
                                "sl_width" to Float::class.java,
                                "sl_height" to Float::class.java,
                                "sl_initialLocation" to Float::class.java,
                                "sl_firstCut" to Float::class.java,
                                "sl_secondCut" to Float::class.java,
                                "sl_movementSpeed" to Float::class.java,
                                "sl_TriggerCurveEdgeCount" to Int::class.java,
                            ),
                            constraints = mapOf(),
                            drawing = SettingsArt.EntryForType,
                            specialDrawing = mapOf(),
                            saveValues = PrefValues.changedValuesMap
                        )
                    }
                }
            }

        }
        FloatingActionButton(
            onClick = {
                PrefValues.save(context)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset { IntOffset(-20, -20) },
        ) {
            Text(text = "Save")
        }
    }
}