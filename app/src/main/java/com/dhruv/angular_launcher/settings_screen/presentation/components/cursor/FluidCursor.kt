package com.dhruv.angular_launcher.settings_screen.presentation.components.cursor

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.core.database.room.ThemeDatabase
import com.dhruv.angular_launcher.settings_screen.ThemeUIEvent
import com.dhruv.angular_launcher.settings_screen.presentation.components.LabelForEnum
import com.dhruv.angular_launcher.settings_screen.presentation.components.LabelForFloat


@Composable
fun FluidCursor (
) {

    val context = LocalContext.current
    val resources = context.resources
    val vm = remember { ThemeDatabase.getViewModel(context) }
    val theme = vm.currTheme
    LazyColumn() {
        item {
//            LabelForColor(label = "color", androidx.compose.ui.graphics.Color.White) {
//                vm.onUIInput(
//                    ThemeUIEvent.UpdateCurrentTheme(
//                        resources,
//                        theme.copy(cursorColor = it.toString())
//                    )
//                )
//            }

            Spacer(modifier = Modifier.height(10.dp))

            LabelForFloat(key = "free radius", min = 0f, value = theme.freeRadius, max = 100f) {
                vm.onUIInput(
                    ThemeUIEvent.UpdateCurrentTheme(
                        resources,
                        theme.copy(freeRadius = it)
                    )
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            LabelForFloat(
                key = "app stuck radius",
                min = 0f,
                value = theme.appStuckRadius,
                max = 100f
            ) {
                vm.onUIInput(
                    ThemeUIEvent.UpdateCurrentTheme(
                        resources,
                        theme.copy(appStuckRadius = it)
                    )
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            LabelForFloat(
                key = "slider stuck radius",
                min = 0f,
                value = theme.sliderStuckRadius,
                max = 100f
            ) {
                vm.onUIInput(
                    ThemeUIEvent.UpdateCurrentTheme(
                        resources,
                        theme.copy(sliderStuckRadius = it)
                    )
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            LabelForEnum(
                key = "movement mode",
                value = theme.cursorSlidingMode,
                options = mapOf(
                    0 to Pair("at touch", "no animations"),
                    1 to Pair("tween", "move towards touch in constant time"),
                    2 to Pair("spring 1", "weak springy motion"),
                    3 to Pair("spring 2", "weak springy motion"),
                    4 to Pair("spring 3", "weak springy motion"),
                )
            ) {
                vm.onUIInput(
                    ThemeUIEvent.UpdateCurrentTheme(
                        resources,
                        theme.copy(cursorSlidingMode = it)
                    )
                )
            }
        }
    }
}
