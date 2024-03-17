package com.dhruv.angular_launcher.core.database.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhruv.angular_launcher.core.database.room.dao.AppDataDao
import com.dhruv.angular_launcher.core.database.room.dao.GroupAppCrossRefDao
import com.dhruv.angular_launcher.core.database.room.dao.GroupDataDao
import com.dhruv.angular_launcher.core.database.room.models.AppData
import com.dhruv.angular_launcher.core.database.room.models.GroupAppCrossRef
import com.dhruv.angular_launcher.core.database.room.models.GroupData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AppDatabaseVM(
    private val appDataDao: AppDataDao,
    private val groupDataDao: GroupDataDao,
    private val groupAppCrossRefDao: GroupAppCrossRefDao
) : ViewModel() {
    val apps = appDataDao.getAllAppsLiveData()
    val visibleApps = appDataDao.getAllVisibleApps()
    val groups = groupDataDao.getAllGroups()

    val appsStartingChars   = visibleApps.map { it.map { getFirstChar(it.name) }.toSet().sorted() }
    val appsByChar          = visibleApps.map { list ->
        val map = mutableMapOf<String, MutableList<AppData>>()
        list.forEach { appData ->
            val firstChar = getFirstChar(appData.name)
            if (map.containsKey(firstChar)) {
                map[firstChar]!!.add(appData)
            } else {
                map[firstChar] = mutableListOf(appData)
            }
        }
        map.toMap()
    }
    val groupIds            = groups.map { it.map { it._id } }
    @OptIn(ExperimentalCoroutinesApi::class)
    val appsByGroup         = groups.flatMapLatest { groups ->
        flow {
            val appsByGroup = mutableMapOf<String, List<AppData>>()
            groups.forEach { group ->
                println("group:->  ${group.name}")
                groupAppCrossRefDao.getAppsForGroup(group._id).collect { appsList ->
                    appsByGroup[group._id.toString()] = appsList
                    println("for ${group._id} : ${appsList.map { it.name }}")
                }
            }
            emit(appsByGroup.toMap())
        }
    }

    fun insertApp(appData: AppData) {
        viewModelScope.launch (Dispatchers.IO) {
            appDataDao.insert(appData)
        }
    }

    fun updateApp(appData: AppData) {
        viewModelScope.launch (Dispatchers.IO) {
            appDataDao.update(appData)
        }
    }

    // Functions for GroupDataDao
    fun insertGroup(groupData: GroupData) {
        viewModelScope.launch (Dispatchers.IO) {
            groupDataDao.insert(groupData)
        }
    }
    fun updateGroup(groupData: GroupData) {
        viewModelScope.launch (Dispatchers.IO) {
            groupDataDao.update(groupData)
        }
    }

    // Functions for GroupAppCrossRefDao
    fun getAppsForGroup(groupId: Int): Flow<List<AppData>> {
        return groupAppCrossRefDao.getAppsForGroup(groupId)
    }
    // Functions for GroupAppCrossRefDao
    fun getVisibleAppsForGroup(groupId: Int): Flow<List<AppData>> {
        return groupAppCrossRefDao.getVisibleAppsForGroup(groupId)
    }
    fun getGroupsForApp(appId: String): Flow<List<GroupData>> {
        return groupAppCrossRefDao.getGroupsForApp(appId)
    }

    fun deleteGroupApps(groupId: Int, apps: List<String>){
        viewModelScope.launch (Dispatchers.IO) {
            groupAppCrossRefDao.deleteExtraConnections(groupId, apps)
        }
    }

    fun insertConnection(connection: GroupAppCrossRef){
        viewModelScope.launch (Dispatchers.IO) {
            groupAppCrossRefDao.insert(connection)
        }
    }

    fun deleteConnection(connection: GroupAppCrossRef){
        viewModelScope.launch (Dispatchers.IO) {
            groupAppCrossRefDao.delete(connection)
        }
    }

    private fun getFirstChar (string: String): String {
        return if (string.first().isLetter()) string.first().uppercase() else "#"
    }

    fun deleteGroup(group: GroupData) {
        viewModelScope.launch (Dispatchers.IO) {
            groupDataDao.delete(group)
        }
    }
}