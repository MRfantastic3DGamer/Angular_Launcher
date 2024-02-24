package com.dhruv.angular_launcher.settings_screen

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhruv.angular_launcher.apps_data.MyApplication
import com.dhruv.angular_launcher.apps_data.model.AppData
import com.dhruv.angular_launcher.apps_data.model.GroupData
import com.dhruv.angular_launcher.settings_module.prefferences.values.PrefValues
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsVM : ViewModel() {
    var settingsOpened by mutableStateOf(false)
    private val realm = MyApplication.realm

    val appsState = realm
        .query<AppData>()
        .asFlow()
        .map { res -> res.list.toList() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val groupsState = realm
        .query<GroupData>()
        .asFlow()
        .map { res -> res.list.toList() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val _values: MutableMap<String, MutableState<Any>?> = mutableMapOf()

    fun tryToGetState (key: String): MutableState<Any>? {
        if (_values.containsKey(key)){
            return _values[key]!!
        }

        try {
            val variable = PrefValues::class.java.getDeclaredField(key)
            variable.isAccessible = true
            _values[key] = mutableStateOf(variable.get(PrefValues))
            return _values[key]
        }catch (error: Throwable){
            println("no reflection for $key")
            return null
        }
    }

    fun saveGroup(data: GroupData) {
        viewModelScope.launch {
            realm.write {
                val latestApps = mutableListOf<AppData>()
                data.apps.forEach {
                    latestApps.add(findLatest(it) ?: it)
                }
                data.apply {
                    apps.clear()
                    apps.addAll(latestApps)
                }
                copyToRealm(data)
            }
        }
    }

    suspend fun save(context: Context){
        return withContext(Dispatchers.IO){
            _values.keys.forEach {
                PrefValues.changedValuesMap[it] = _values[it]!!.value!!
            }
            PrefValues.save(context)
        }
    }
}