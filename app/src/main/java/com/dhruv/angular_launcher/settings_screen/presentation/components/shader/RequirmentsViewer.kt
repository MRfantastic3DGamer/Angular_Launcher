package com.dhruv.angular_launcher.settings_screen.presentation.components.shader

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.core.resources.AllResources
import com.dhruv.angular_launcher.settings_screen.presentation.components.H2
import com.dhruv.angular_launcher.settings_screen.presentation.components.H3

fun LazyListScope.requirementsViewer(
    onSelect: (AllResources)->Unit,
    selected: Set<String>
){
    items(AllResources.entries, key = {it}){
        Column (
            Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp)
                .background( if (selected.contains(it.name)) Color.Gray else Color.Black )
                .clickable { onSelect(it) }
        ) {
            H2(text = it.name)
            H3(text = "todo")
        }
    }
}