package com.dhruv.angular_launcher.settings_screen

import android.content.ContentResolver
import android.content.Context
import com.dhruv.angular_launcher.core.database.room.models.ThemeData
import com.dhruv.angular_launcher.core.resources.ShaderData

sealed interface ThemeUIEvent {
    class SaveShaderToCurrentTheme(val contentResolver: ContentResolver, val shaderData: ShaderData) : ThemeUIEvent
    class ApplyTheme(val context: Context, val contentResolver: ContentResolver, val id: Int) : ThemeUIEvent
    class SaveTheme(val contentResolver: ContentResolver, val data: ThemeData) : ThemeUIEvent
    class UpdateCurrentTheme(val contentResolver: ContentResolver, val data: ThemeData): ThemeUIEvent
    class SaveCopyTheme(val contentResolver: ContentResolver, val data: ThemeData): ThemeUIEvent
    class DeleteTheme(val id: Int): ThemeUIEvent
}