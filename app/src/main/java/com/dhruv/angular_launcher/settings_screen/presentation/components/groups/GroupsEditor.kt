package com.dhruv.angular_launcher.settings_screen.presentation.components.groups

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.database.room.models.AppData
import com.dhruv.angular_launcher.database.room.models.GroupData

@Composable
fun GroupsEditor (vm: GroupsEditingVM){

    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    val groups = emptyList<GroupData>()
    val apps = emptyList<AppData>()
    val selectedApps by remember { mutableStateOf(mutableSetOf<AppData>()) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    vm.newGroup()
                    showDialog = true
                },
                content = { Icon(Icons.Default.Add, contentDescription = "Add Group") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text("Created Groups:")
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                itemsIndexed(groups) { index, group ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .clickable {
                                vm.getGroup(index)
                                showDialog = true
                            }
                    ) {
                        Text(group.name, color = Color.Blue)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.Edit, contentDescription = "Edit Group")
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                vm.groupName = TextFieldValue()
            },
            title = {
                Text(if (vm.selectedGroup != null) "Edit Group" else "Create Group")
            },
            text = {
                Column {
                    TextField(
                        value = vm.groupName,
                        onValueChange = { vm.groupName = it },
                        label = { Text("Enter group name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Select strings to add to group:")
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn {
                        items(apps) { app ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                Checkbox(
                                    checked = selectedApps.contains(app),
                                    onCheckedChange = { isChecked ->
                                        if (isChecked) {
                                            selectedApps.remove(app)
                                        } else {
                                            selectedApps.add(app)
                                        }
                                    }
                                )
                                Text(app.name)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (vm.groupName.text.isNotBlank()) {
                            vm.selectedGroup!!.apply {
                                name = vm.groupName.text
                            }
                            vm.saveGroup()
                            showDialog = false
                            vm.selectedGroup = null
                            vm.groupName = TextFieldValue()
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false
                        vm.groupName = TextFieldValue()
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}