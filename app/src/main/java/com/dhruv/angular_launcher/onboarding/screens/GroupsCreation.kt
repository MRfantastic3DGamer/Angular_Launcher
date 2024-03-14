package com.dhruv.angular_launcher.onboarding.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.core.appIcon.StaticAppIcon
import com.dhruv.angular_launcher.core.database.room.AppDatabase

@Composable
fun GroupsCreation() {
    val context = LocalContext.current
    val DBVM = AppDatabase.getViewModel(context)
    val apps = DBVM.apps.collectAsState(initial = emptyList()).value
    Column(
        modifier = Modifier
            .fillMaxSize(),
        Arrangement.SpaceEvenly,
        Alignment.End
    ) {
        Row {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(50.dp)
            ) {
                LazyColumn {
                    items(apps){
                        StaticAppIcon(packageName = it.packageName, size = 40)
                    }
                }
            }
            Box(
                modifier =Modifier
                    .fillMaxHeight()
                    .width(50.dp)
            ) {
                
            }
        }
    }
}

@Preview
@Composable
private fun GroupsPrev() {
    GroupsCreation()
}