package com.dhruv.angular_launcher.settings_screen.presentation.components.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.accessible_screen.components.glsl_art.ShadersFor_GettingBasicDataToDrawStuff
import com.dhruv.angular_launcher.core.database.room.ThemeDatabase
import com.dhruv.angular_launcher.settings_screen.ThemeUIEvent
import com.dhruv.angular_launcher.settings_screen.presentation.components.H1
import com.dhruv.angular_launcher.settings_screen.presentation.components.H2
import com.dhruv.angular_launcher.settings_screen.presentation.components.H3


val MainImageWidth = 200
val ImageWidth = 100

typealias themeIdAndName = Pair<Int?, String>

@Composable
fun Theme (
) {
    val context = LocalContext.current
    val contentResolver = context.contentResolver
    val vm = remember { ThemeDatabase.getViewModel(context) }
    val themes: List<themeIdAndName> = vm.themes.collectAsState(initial = emptyList()).value.map { Pair(it._id, it.name) }
    val state = vm.currTheme

    Column {

        H1(text = "Themes")
        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn {


            item {
                H2(text = "Getting basic data from launcher and system")
            }
            item {
                LazyRow {
                    items(
                        ShadersFor_GettingBasicDataToDrawStuff,
                        key = { it.name }
                    ){
                        Card (
                            modifier = Modifier
                                .width(200.dp)
                                .height(100.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { vm.onUIInput(ThemeUIEvent.SaveShaderToCurrentTheme(contentResolver, it)) }
                        ) {
                            H3(text = it.name)
                        }
                    }
                }
            }

            item {
                H2(text = "My themes")
            }
            items(
                themes,
                key = { it.first ?: 0 }
            ) {
                Card (
                    modifier = Modifier
                        .clickable {
                            vm.onUIInput(ThemeUIEvent.ApplyTheme(context, contentResolver, it.first ?: 0))
                        }
                ) {
                    Row(
                        Modifier,
                        Arrangement.SpaceBetween,
                        Alignment.CenterVertically
                    ) {
                        Text(text = it.second)
                        IconButton(onClick = { vm.onUIInput(ThemeUIEvent.DeleteTheme(it.first ?: 0)) }) {
                            Icon(imageVector = Icons.Outlined.Delete, contentDescription = "delete", tint = Color.Red)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                Button(onClick = {
                    vm.onUIInput(ThemeUIEvent.SaveCopyTheme(contentResolver, vm.currTheme))
                }) {
                    Text(text = "Save")
                }
            }
        }
    }
}