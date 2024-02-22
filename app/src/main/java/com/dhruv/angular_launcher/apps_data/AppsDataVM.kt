package com.dhruv.angular_launcher.apps_data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhruv.angular_launcher.apps_data.model.AppData
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
}