package com.dhruv.angular_launcher.settings_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import java.lang.reflect.Type

object SettingsArt {
    val EntryForType = mapOf<Type, @Composable (String, Any, Pair<Float, Float>?, (Any) -> Unit) -> Unit>(
        Float::class.java to { key, value, constraints, update ->
            Column {
                Text(text = key)
                if (constraints != null){
                    Slider(
                        value = value.toString().toFloat(),
                        onValueChange = { update(it) },
                        valueRange = constraints.first..constraints.second,
                    )
                }
                else{
                    TextField(value = value.toString(), onValueChange = { update(it) }, maxLines = 1)
                }
            }
        },
        Int::class.java to { key, value, constraints, update ->
            Column {
                Text(text = key)
                if (constraints != null){
                    Slider(
                        value = value.toString().toFloat(),
                        onValueChange = { update(it) },
                        valueRange = constraints.first..constraints.second,
                    )
                }
                else{
                    TextField(value = value.toString(), onValueChange = { update(it) }, maxLines = 1)
                }
            }
        }
    )
}