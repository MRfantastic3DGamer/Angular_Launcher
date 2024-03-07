package com.dhruv.angular_launcher.accessible_screen.components.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.dhruv.angular_launcher.core.database.room.AppDatabase

@Composable
fun SearchApps () {
    val context = LocalContext.current
    val DBVM = AppDatabase.getViewModel(context)
    var searchQuery by remember { mutableStateOf("") }
    val apps = DBVM.visibleApps.collectAsState(initial = emptyList()).value.filter { it.packageName.contains(searchQuery, ignoreCase = true) || it.name.contains(searchQuery, ignoreCase = true) }


}