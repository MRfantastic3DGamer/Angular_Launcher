package com.dhruv.angular_launcher.onboarding.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Introduction() {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(80.dp),
        Alignment.Center
    ) {
        Text(text = "Welcome to Angular launcher", color = Color.White)
    }
}