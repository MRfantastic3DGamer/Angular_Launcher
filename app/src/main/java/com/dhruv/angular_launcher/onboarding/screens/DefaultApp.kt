package com.dhruv.angular_launcher.onboarding.screens

import android.content.Intent
import android.provider.Settings.ACTION_HOME_SETTINGS
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun DefaultApp (){
    val context = LocalContext.current
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(80.dp),
        Alignment.Center
    ) {
        Column {
            Text(text = "Set Angular launcher as your default home app", color = Color.White)
            Button(onClick = {
                val intent = Intent( ACTION_HOME_SETTINGS)
                context.startActivity(intent)
            }) {
                Text(text = "Accept")
            }
        }
    }
}