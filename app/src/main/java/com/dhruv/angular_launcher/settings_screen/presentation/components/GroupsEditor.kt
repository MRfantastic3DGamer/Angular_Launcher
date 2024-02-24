package com.dhruv.angular_launcher.settings_screen.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.TextUnit
import com.dhruv.angular_launcher.apps_data.model.AppData
import com.dhruv.angular_launcher.apps_data.model.GroupData

@Composable
fun GroupsEditor (appPackages: List<AppData>, groups: MutableList<GroupData>, saveGroup: (GroupData)->Unit){
    var groupName by remember { mutableStateOf(TextFieldValue()) }
    val selectedApps by remember { mutableStateOf(mutableListOf<AppData>()) }
    var editingGroupIndex by remember { mutableStateOf(-1) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDialog = true
                    selectedApps.clear()
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
                                editingGroupIndex = index
//                                group.name.trim().split(",").forEach { selectedApps.add(it.trim()) }
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
                editingGroupIndex = -1
                groupName = TextFieldValue()
                selectedApps.clear()
            },
            title = {
                Text(if (editingGroupIndex >= 0) "Edit Group" else "Create Group")
            },
            text = {
                Column {
                    TextField(
                        value = groupName,
                        onValueChange = { groupName = it },
                        label = { Text("Enter group name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Select strings to add to group:")
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn {
                        items(appPackages) { app ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                Checkbox(
                                    checked = selectedApps.contains(app),
                                    onCheckedChange = { isChecked ->
                                        if (isChecked) {
                                            selectedApps.add(app)
                                        } else {
                                            selectedApps.remove(app)
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
                        if (groupName.text.isNotBlank()) {
                            if (editingGroupIndex >= 0) {
                                groups[editingGroupIndex].apply {
                                    name = groupName.text
                                    apps.clear()
                                    apps.addAll(selectedApps)
                                }
                                saveGroup(groups[editingGroupIndex])
                            } else {
                                val newGroup = GroupData().apply {
                                    name = groupName.text
                                    apps.addAll(selectedApps)
                                }
                                groups.add(newGroup)
                                saveGroup(newGroup)
                            }
                            showDialog = false
                            editingGroupIndex = -1
                            groupName = TextFieldValue()
                            selectedApps.clear()
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
                        editingGroupIndex = -1
                        groupName = TextFieldValue()
                        selectedApps.clear()
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}