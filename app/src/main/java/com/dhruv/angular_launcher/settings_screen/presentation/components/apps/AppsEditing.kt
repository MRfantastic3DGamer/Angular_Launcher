package com.dhruv.angular_launcher.settings_screen.presentation.components.apps

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.R
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.presentation.withAlpha
import com.dhruv.angular_launcher.core.appIcon.StaticAppIcon
import com.dhruv.angular_launcher.settings_screen.presentation.components.H1
import com.dhruv.angular_launcher.settings_screen.presentation.components.H2

@Composable
fun AppsEditing (vm: AppsEditingVM) {

    val context = LocalContext.current
    val visibilityImage = painterResource(id = R.drawable.baseline_visibility_24)
    val visibilityOffImage = painterResource(id = R.drawable.baseline_visibility_off_24)

    LazyColumn(
        Modifier
            .fillMaxSize()
    ){
        item {
            Row (
                Modifier.padding(16.dp),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically,
            ) {
                H1(text = "Apps")
                IconButton(onClick = { vm.ascending = !vm.ascending }) {
                    Icon(imageVector = if (vm.ascending) Icons.Outlined.KeyboardArrowDown else Icons.Outlined.KeyboardArrowUp, contentDescription = "sort")
                }
            }
        }
        items(
            if (vm.ascending) vm.apps else vm.apps.reversed(),
            key = {it.packageName}
        ){
            Box {
                Column {
                    Row (
                        Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                            .height(30.dp),
                        Arrangement.SpaceBetween,
                        Alignment.CenterVertically,
                    ) {
                        Row (
                            Modifier
                                .clickable {
                                    vm.selectedApp.value = it
                                    vm.appName = TextFieldValue(it.name)
                                    vm.appVisible = it.visible
                                    vm.showPopup.value = true
                                }
                            ,
                            Arrangement.SpaceBetween,
                            Alignment.CenterVertically,
                        ) {
                            StaticAppIcon(packageName = it.packageName, size = 80)
                            H2(text = it.name)
                        }
                        Icon(
                            painter = if (it.visible) visibilityImage else visibilityOffImage,
                            contentDescription = "Visibility",
                            Modifier
                                .size(32.dp)
                                .clickable {
                                    vm.selectedApp.value = it
                                    vm.appName = TextFieldValue(it.name)
                                    vm.appVisible = !it.visible
                                    vm.saveApp()
                                },
                            Color.White
                        )
                    }
                    Divider(Modifier.padding(4.dp))
                }
                if (!it.visible){
                    Box(
                        modifier = Modifier
                            .height(60.dp)
                            .fillMaxWidth()
                            .background(Color.Black.withAlpha(0.3f))
                    )
                }
            }
        }
    }
    if (vm.showPopup.value){
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Edit App appearance") },
            text = {
                Column {
                    TextField(
                        value = vm.appName,
                        onValueChange = { vm.appName = it },
                        label = { Text("Enter app name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (vm.appName.text.isNotBlank()) {
                            vm.saveApp()
                        }
                        vm.showPopup.value = false
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        vm.showPopup.value = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}