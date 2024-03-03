package com.dhruv.angular_launcher.settings_screen.presentation.components.groups

import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.R
import com.dhruv.angular_launcher.core.database.room.AppDatabase
import com.dhruv.angular_launcher.core.database.room.models.GroupAppCrossRef
import com.dhruv.angular_launcher.core.database.room.models.GroupData
import com.dhruv.angular_launcher.settings_screen.presentation.components.H2

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
                onClick = { DBVM.insertGroup(GroupData(name = "group ${groups.size}", iconKey = R.drawable.group_social.toString())) },
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
                    Group(group) { vm.getGroup(group) }
                }
            }
        }
    }

    if (vm.showGroupEditingDialog) {
        AlertDialog(
            onDismissRequest = { vm.dismiss() },
            title = { Text(if (vm.selectedGroup != null) "Edit Group" else "Create Group") },
            text = {
                Column {
                    Row {
                        IconButton(onClick = { vm.showGroupIconChoices = !vm.showGroupIconChoices }) {
                            val image = BitmapFactory.decodeResource( context.resources, vm.keyValue.text.toInt()).asImageBitmap()
                            Image(
                                bitmap = image,
                                contentDescription = R.drawable.group_writing.toString(),
                                Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color.White),
                                Alignment.Center,
                                ContentScale.Fit,
                            )
                        }
                        TextField(
                            value = vm.nameValue,
                            onValueChange = { vm.nameValue = it },
                            label = { Text("Enter group name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    AnimatedVisibility(visible = vm.showGroupIconChoices) {
                        GroupIconChoices{
                            vm.keyValue = TextFieldValue(it.toString())
                            vm.showGroupIconChoices = false
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Select apps to add to group:")
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn {
                        items(
                            apps,
                            key = {it.packageName}
                        ){ app ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                Checkbox(
                                    checked = appsOfCurrentGroup.contains(app.packageName),
                                    onCheckedChange = { isChecked ->
                                        if (isChecked) {
                                            DBVM.insertConnection(GroupAppCrossRef(vm.selectedGroup!!._id, app.packageName))
//                                            println("inserted ${GroupAppCrossRef(vm.selectedGroup!!._id, app.packageName)}")
                                        } else {
                                            DBVM.deleteConnection(GroupAppCrossRef(vm.selectedGroup!!._id, app.packageName))
//                                            println("removed ${GroupAppCrossRef(vm.selectedGroup!!._id, app.packageName)}")
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
                        vm.showGroupEditingDialog = false
                        val new = vm.selectedGroup?.copy(name = vm.nameValue.text, iconKey = vm.keyValue.text)
                        println(new)
                        if (new != null) { DBVM.updateGroup(new) }
                    }
                ) { Text(text = "Save") }
            },
            dismissButton = {},
        )
    }
}

@Composable
fun Group(
    data: GroupData,
    select: ()->Unit
) {
    val context = LocalContext.current
    Row (
        Modifier
            .clickable { select() },
        Arrangement.Start,
        Alignment.CenterVertically,
    ) {
        val image =
            BitmapFactory.decodeResource(context.resources, data.iconKey.toInt())
                .asImageBitmap()
        Image(
            bitmap = image,
            contentDescription = R.drawable.group_writing.toString(),
            Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White),
            Alignment.Center,
            ContentScale.Fit,
        )
        H2(text = data.name)
    }
}

@Composable
fun GroupIconChoices(onSelect: (Int)->Unit) {
    val context = LocalContext.current
    val icons = GroupIcons.allGroupIcons

    LazyVerticalGrid (
        columns = GridCells.Adaptive( minSize = 48.dp ),
        Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White),
    ) {
        items(
            icons,
            key = { it.toString() }
        ){ icon ->
            Image(
                bitmap = BitmapFactory.decodeResource(context.resources, icon).asImageBitmap(),
                contentDescription = icon.toString(),
                Modifier
                    .size(48.dp)
                    .padding(2.dp)
                    .clickable { onSelect(icon) }
                ,
                Alignment.Center,
                ContentScale.Inside,
                colorFilter = ColorFilter.lighting(Color.Black, Color.Black)
            )
        }
    }
}

@Preview
@Composable
fun iconsPrev() {
    GroupIconChoices(onSelect = {})
}