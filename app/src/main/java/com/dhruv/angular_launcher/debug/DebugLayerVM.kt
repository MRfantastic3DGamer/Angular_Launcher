package com.dhruv.angular_launcher.debug

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel

class DebugLayerVM: ViewModel() {

    var strings: List<String> by mutableStateOf(listOf())
    var lines: List<Pair<Offset,Offset>> by mutableStateOf(listOf())
    var points: List<Offset> by mutableStateOf(listOf())

    init {
        DebugLayerValues.GetLines.observeForever {
            lines = it
        }
        DebugLayerValues.GetStrings.observeForever {
            strings = it
        }
        DebugLayerValues.GetPoints.observeForever {
            points = it
        }
    }
}