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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.core.database.room.AppDatabase
import com.dhruv.angular_launcher.core.database.room.models.GroupAppCrossRef
import com.dhruv.angular_launcher.core.database.room.models.GroupData

@Composable
fun GroupsEditor (vm: GroupsEditingVM){

    val context = LocalContext.current
    val DBVM = remember{ AppDatabase.getViewModel(context) }
    val groups = DBVM.groups.collectAsState(initial = emptyList()).value
    val apps = DBVM.apps.collectAsState(initial = emptyList()).value
    val appsOfCurrentGroup = (if (vm.selectedGroup != null) DBVM.getAppsForGroup(vm.selectedGroup!!._id).collectAsState(initial = emptyList()).value else emptyList()).map { it.packageName }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { DBVM.insertGroup(GroupData(name = "group ${groups.size}", iconKey = "key")) },
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
                items(groups) {group ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .clickable {
                                vm.getGroup(group)
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

    if (vm.showDialog) {
        AlertDialog(
            onDismissRequest = { vm.dismiss() },
            title = { Text(if (vm.selectedGroup != null) "Edit Group" else "Create Group") },
            text = {
                Column {
                    TextField(
                        value = vm.nameValue,
                        onValueChange = { vm.nameValue = it },
                        label = { Text("Enter group name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = vm.keyValue,
                        onValueChange = { vm.keyValue = it },
                        label = { Text("Enter group iconKey") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Select apps to add to group:")
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn {
                        items(apps){ app ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                Checkbox(
                                    checked = appsOfCurrentGroup.contains(app.packageName),
                                    onCheckedChange = { isChecked ->
                                        if (isChecked) {
                                            DBVM.insertConnection(GroupAppCrossRef(vm.selectedGroup!!._id, app.packageName))
                                        } else {
                                            DBVM.deleteConnection(GroupAppCrossRef(vm.selectedGroup!!._id, app.packageName))
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
                Button(onClick = { vm.save { DBVM.insertGroup(it) } }) {
                    Text(text = "Save")
                }
            },
            dismissButton = {},
        )
    }
}