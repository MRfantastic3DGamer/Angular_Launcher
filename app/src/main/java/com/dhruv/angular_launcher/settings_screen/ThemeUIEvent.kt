package com.dhruv.angular_launcher.settings_screen

import android.content.Context
import android.content.res.Resources
import com.dhruv.angular_launcher.core.resources.ShaderData
import com.dhruv.angular_launcher.core.database.room.models.ThemeData

sealed interface ThemeUIEvent {
    class SaveShaderCode(val resources: Resources, val shaderData: ShaderData) : ThemeUIEvent
    class ApplyTheme(val context: Context, val resources: Resources, val id: Int) : ThemeUIEvent
    class SaveTheme(val resources: Resources, val data: ThemeData) : ThemeUIEvent
    class UpdateCurrentTheme(val resources: Resources, val data: ThemeData): ThemeUIEvent
    class SaveCopyTheme(val resources: Resources, val data: ThemeData): ThemeUIEvent
    class DeleteTheme(val id: Int): ThemeUIEvent
}