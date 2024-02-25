package com.dhruv.angular_launcher.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.dhruv.angular_launcher.database.room.models.AppData
import com.dhruv.angular_launcher.database.room.models.GroupAppCrossRef
import com.dhruv.angular_launcher.database.room.models.GroupData
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupAppCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(groupAppCrossRef: GroupAppCrossRef)

    @Transaction
    @Query("SELECT apps.* FROM apps INNER JOIN group_app_cross_ref ON apps.packageName = group_app_cross_ref.appId WHERE group_app_cross_ref.groupId = :groupId")
    fun getAppsForGroup(groupId: Int): Flow<List<AppData>>

    @Transaction
    @Query("SELECT groups.* FROM groups INNER JOIN group_app_cross_ref ON groups._id = group_app_cross_ref.groupId WHERE group_app_cross_ref.appId = :appId")
    fun getGroupsForApp(appId: String): Flow<List<GroupData>>
}