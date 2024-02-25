package com.dhruv.angular_launcher.database.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhruv.angular_launcher.database.room.dao.AppDataDao
import com.dhruv.angular_launcher.database.room.dao.GroupAppCrossRefDao
import com.dhruv.angular_launcher.database.room.dao.GroupDataDao
import com.dhruv.angular_launcher.database.room.models.AppData
import com.dhruv.angular_launcher.database.room.models.GroupData
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
    val groups = groupDataDao.getAllGroups()

    val appsStartingChars   = apps.map { it.map { getFirstChar(it.name) }.toSet().sorted() }
    val appsByChar          = apps.map { list ->
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
                groupAppCrossRefDao.getAppsForGroup(group._id).collect { apps ->
                    appsByGroup[group._id.toString()] = apps
                    emit(appsByGroup.toMap())
                }
            }
        }
    }

    fun insertApp(appData: AppData) {
        viewModelScope.launch {
            appDataDao.insert(appData)
        }
    }

    fun updateApp(appData: AppData) {
        viewModelScope.launch {
            appDataDao.update(appData)
        }
    }

    suspend fun insertAllApps(apps: List<AppData>){

    }

    // Functions for GroupDataDao
    suspend fun insertGroup(groupData: GroupData) {
        groupDataDao.insert(groupData)
    }

    // Functions for GroupAppCrossRefDao
    fun getAppsForGroup(groupId: Int): Flow<List<AppData>> {
        return groupAppCrossRefDao.getAppsForGroup(groupId)
    }
    fun getGroupsForApp(appId: String): Flow<List<GroupData>> {
        return groupAppCrossRefDao.getGroupsForApp(appId)
    }

    fun getFirstChar (string: String): String {
        return if (string.first().isLetter()) string.first().uppercase() else "#"
    }
}