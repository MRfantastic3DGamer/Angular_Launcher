package com.dhruv.angular_launcher.settings_screen.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.core.database.prefferences.values.PrefValues
import java.lang.reflect.Type


data class EntryData(
    val label: String,
    val data: MutableState<Any>,
    val type: Type,
    val constraints: Pair<Any, Any>? = null,
)
data class SettingsColumnData(
    val heading: String,
    val subHeading: String?,
    val values: List<EntryData>
)

/**
 * It is used to draw and save a list of values that must be predefined in [PrefValues]
 */

@Composable
fun SettingsColumn (
    data: SettingsColumnData,
    entryForType: Map<Type, @Composable (String, MutableState<Any>, Pair<Any, Any>?) -> Unit>
){
    Column (
        Modifier.fillMaxWidth()
    ) {

        H1(text = data.heading)

        data.values.forEach {
            entryForType[it.type]?.invoke(it.label, it.data, it.constraints)
            Divider(Modifier.padding(4.dp))
        }
    }
}