package com.dhruv.angular_launcher.settings_screen.presentation.components.apps

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.core.AppIcon.StaticAppIcon
import com.dhruv.angular_launcher.core.database.apps_data.AppsIconsDataValues
import com.dhruv.angular_launcher.settings_screen.presentation.components.H2

@Composable
fun AppsEditing (vm: AppsEditingVM) {

    val icons = AppsIconsDataValues.getAppsIcons.value

    LazyColumn(
        Modifier
            .fillMaxSize()
    ){
        items(
            vm.apps,
            key = {it.packageName}
        ){
            Row (
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically,
            ) {
                Row (
                    Modifier.clickable {
                        vm.selectedApp.value = it
                        vm.appName = TextFieldValue(it.name)
                        vm.appVisible = it.visible
                        vm.showPopup.value = true
                    },
                    Arrangement.SpaceBetween,
                    Alignment.CenterVertically,
                ) {
                    StaticAppIcon(packageName = it.packageName, size = 80)
                    H2(text = it.name)
                }
                Icon(
                    imageVector = if (it.visible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = "Visibility",
                    Modifier.clickable {
                        vm.selectedApp.value = it
                        vm.appName = TextFieldValue(it.name)
                        vm.appVisible = !it.visible
                        vm.saveApp()
                    },
                    Color.White,
                )
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