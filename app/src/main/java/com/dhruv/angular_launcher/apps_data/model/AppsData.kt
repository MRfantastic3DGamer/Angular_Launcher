package com.dhruv.angular_launcher.apps_data.model

import android.content.Intent
import androidx.compose.runtime.Stable
import com.example.launcher.Drawing.DrawablePainter

@Stable
data class AppsData(
    val names: List<String?> = listOf(),
    val packages: List<String?> = listOf(),
    val icons: List<DrawablePainter?> = listOf(),
)