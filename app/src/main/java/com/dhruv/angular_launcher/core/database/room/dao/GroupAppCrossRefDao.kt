package com.dhruv.angular_launcher.core.database.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.dhruv.angular_launcher.core.database.room.models.AppData
import com.dhruv.angular_launcher.core.database.room.models.GroupAppCrossRef
import com.dhruv.angular_launcher.core.database.room.models.GroupData
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupAppCrossRefDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(groupAppCrossRef: GroupAppCrossRef)
    @Transaction
    @Delete
    suspend fun delete(connection: GroupAppCrossRef)

    @Transaction
    @Query("SELECT apps.* FROM apps INNER JOIN group_app_cross_ref ON apps.packageName = group_app_cross_ref.appId WHERE group_app_cross_ref.groupId = :groupId")
    fun getAppsForGroup(groupId: Int): Flow<List<AppData>>

    @Transaction
    @Query("SELECT groups.* FROM groups INNER JOIN group_app_cross_ref ON groups._id = group_app_cross_ref.groupId WHERE group_app_cross_ref.appId = :appId")
    fun getGroupsForApp(appId: String): Flow<List<GroupData>>

    @Transaction
    @Query("DELETE FROM group_app_cross_ref WHERE groupId = :groupId AND appId NOT IN (:apps)")
    suspend fun deleteExtraConnections(groupId: Int, apps: List<String>)

    @Transaction
    suspend fun updateConnections(groupId: Int, appIds: List<String>) {
        appIds.forEach { appId ->
            insert(GroupAppCrossRef(groupId, appId))
        }
    }
}
