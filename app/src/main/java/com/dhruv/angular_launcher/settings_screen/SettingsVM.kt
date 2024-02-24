package com.dhruv.angular_launcher.settings_screen

import android.content.Context
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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsVM private constructor (
    allKeys: List<String>,
): ViewModel() {
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

    val values = allKeys.associateWith { key ->
        try {
            val variable = PrefValues::class.java.getDeclaredField(key)
            variable.isAccessible = true
            mutableStateOf(variable.get(PrefValues))
        }catch (error: Throwable){
            println("no reflection for $key")
            null
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

    fun save(context: Context){
        values.keys.forEach {
            PrefValues.changedValuesMap[it] = values[it]!!.value!!
        }
        PrefValues.save(context)
    }


    companion object {
        @Volatile
        private var instance: SettingsVM? = null

        fun getInstance(allKeys: List<String>): SettingsVM {
            return instance ?: synchronized(this) {
                instance ?: SettingsVM(allKeys).also { instance = it }
            }
        }
    }
}