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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppsEditing (vm: AppsEditingVM) {
    LazyColumn(
        Modifier
            .fillMaxSize()
    ){
        items(vm.apps){
            Row (
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                Arrangement.SpaceBetween
            ) {
                Text(
                    text = it.name,
                    Modifier.clickable {
                        vm.selectedApp.value = it
                        vm.appName = TextFieldValue(it.name)
                        vm.appVisible = it.visible
                        vm.showPopup.value = true
                    }
                )
                Icon(
                    imageVector = if (it.visible) Icons.Filled.ArrowForward else Icons.Filled.ArrowBack,
                    contentDescription = "edit",
                    Modifier.clickable {
                        vm.selectedApp.value = it
                        vm.appName = TextFieldValue(it.name)
                        vm.appVisible = !it.visible
                        vm.saveApp()
                    }
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