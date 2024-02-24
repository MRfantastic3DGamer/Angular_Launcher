package com.dhruv.angular_launcher.apps_data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhruv.angular_launcher.apps_data.model.AppData
import com.dhruv.angular_launcher.apps_data.model.GroupData
import com.dhruv.angular_launcher.debug.DebugLayerValues
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppsDataVM : ViewModel() {
    private val realm = MyApplication.realm

    val apps = realm
        .query<AppData>()
        .asFlow()
        .map { res -> res.list.toList() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val groups = realm
        .query<GroupData>()
        .asFlow()
        .map { res -> res.list.toList() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val appsByFirstChar: Map<Char, List<AppData>>
        get() {
            val map = mutableMapOf<Char, MutableList<AppData>>()
            apps.value.forEach {
                if (it.name.first() in map){
                    map[it.name.first()]!!.add(it)
                }
                else{
                    map[it.name.first()] = mutableListOf(it)
                }
            }
            return map
        }

    fun addApp (pckgName: String, appName: String){
        viewModelScope.launch {
            realm.write {
                val newAppData = AppData().apply {
                    packageName = pckgName
                    name = appName
                }
                copyToRealm(newAppData, updatePolicy = UpdatePolicy.ALL)
            }
        }
    }

    /**
     * erases the previously stored app names
     */
    fun addApps(apps: List<AppData>) {
        viewModelScope.launch {
            realm.write {
                apps.forEach { app ->
                    DebugLayerValues.addString(app.name, app.packageName)
                    copyToRealm(app, UpdatePolicy.ALL)
                }
            }
        }
    }
}