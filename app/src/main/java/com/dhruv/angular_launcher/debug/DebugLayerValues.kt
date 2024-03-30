package com.dhruv.angular_launcher.debug

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object DebugLayerValues {
    private val stringsMap = mutableMapOf<String, String>()
    private val linesMap = mutableMapOf<String,Pair<Offset,Offset>>()
    private val pointsMap= mutableMapOf<String, Offset>()

    private val strings: MutableLiveData<List<String>> = MutableLiveData(listOf())
    private val lines: MutableLiveData<List<Pair<Offset,Offset>>> = MutableLiveData(listOf())
    private val points: MutableLiveData<List<Offset>> = MutableLiveData(listOf())

    val GetStrings: LiveData<List<String>>
        get() = strings
    val GetLines: LiveData<List<Pair<Offset,Offset>>>
        get() = lines
    val GetPoints: LiveData<List<Offset>>
        get() = points

    fun addString (value: String, key: String){
        stringsMap[key] = value
        strings.value = stringsMap.map { "${it.key}: ${it.value}" }
    }
    fun addLine (value: Pair<Offset,Offset>, key: String){
        linesMap[key] = value
        lines.value = linesMap.values.toList()
    }
    fun addPoint (value: Offset, key: String){
        pointsMap[key] = value
        points.value = pointsMap.values.toList()
    }
}