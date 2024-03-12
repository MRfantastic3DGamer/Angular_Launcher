package com.dhruv.angular_launcher.accessible_screen.components.app_label.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType

@Composable
fun AppName(
    text: String?
){
    Text(
        text = text ?: "app name not found",
        maxLines = 1,
        fontSize = TextUnit(type = TextUnitType.Sp, value = 20f)
    )
}