package com.dhruv.angular_launcher.settings_screen.presentation.components.groups

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.dhruv.angular_launcher.database.room.models.AppData
import com.dhruv.angular_launcher.database.room.models.GroupData

class GroupsEditingVM: ViewModel() {


    var groupName by mutableStateOf(TextFieldValue())
    var selectedGroup: GroupData? by mutableMapOf()
    var creatingNew: Boolean by mutableStateOf(false)

    fun addApp (app: AppData){
//        if (selectedGroup != null) {
//            selectedGroup!!.apps.add(app)
//        }
//        println(selectedGroup!!.apps.toList())
    }
    fun removeApp (app: AppData) {
        if (selectedGroup == null) return
//        viewModelScope.launch(Dispatchers.IO) {
//            realm.write {
//                val latestG = findLatest(selectedGroup!!) ?: selectedGroup!!
//                val latestA = findLatest(app) ?: app
//                latestG.apps.remove(latestA)
//            }
//        }
    }
    fun newGroup () {
        creatingNew = true
//        viewModelScope.launch(Dispatchers.IO) {
//            realm.write {
//                selectedGroup = GroupData().apply {
//                    name = "new group"
//                    apps = realmSetOf()
//                }
//                copyToRealm(selectedGroup!!)
//            }
//        }
    }
    fun getGroup (index: Int) {
        creatingNew = false
//        viewModelScope.launch(Dispatchers.IO) {
//            realm.write {
//                selectedGroup = findLatest(groups.value[index])
//            }
//        }
    }

    fun saveGroup() {
        if (selectedGroup == null) return
//        viewModelScope.launch(Dispatchers.IO){
//            realm.write {
//                val partApps = selectedGroup!!.apps.toList()
//                val latestG = findLatest(selectedGroup!!) ?: selectedGroup!!
//                latestG.apply {
//                    name = groupName.text
//                    apps.clear()
//                    partApps.forEach {
//                        apps.add(findLatest(it)?:it)
//                    }
//                }
//                copyToRealm(latestG, UpdatePolicy.ALL)
//            }
//        }
    }
}