package com.dhruv.angular_launcher.settings_screen.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import java.lang.reflect.Type

object _SettingsArt {
    val DefaultEntry = mapOf<Type, @Composable (String, MutableState<Any>, Pair<Any, Any>?) -> Unit>(
        Float::class.java to { key, state, constraints->
            Column {
                Text(text = key)
                if (constraints != null){
                    Slider(
                        value = state.value.toString().toFloat(),
                        onValueChange = { state.value = it.toFloat() },
                        valueRange = constraints.first.toString().toFloat()..constraints.second.toString().toFloat(),
                    )
                }
                else{
                    TextField(value = state.value.toString(), onValueChange = { state.value = it.toFloat() }, maxLines = 1)
                }
            }
        },
        Int::class.java to { key, state, constraints ->
            Column {
                Text(text = key)
                if (constraints != null){
                    Slider(
                        value = state.value.toString().toFloat(),
                        onValueChange = { state.value = it.toInt() },
                        valueRange = constraints.first.toString().toFloat()..constraints.second.toString().toFloat(),
                    )
                }
                else{
                    TextField(value = state.value.toString(), onValueChange = { state.value = it.toInt() }, maxLines = 1)
                }
            }
        },
    )
}