package com.dhruv.angular_launcher.core.database.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dhruv.angular_launcher.core.database.room.models.GroupData
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(groupData: GroupData)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(groupData: GroupData)

    @Delete
    fun delete(group: GroupData)

    @Query("SELECT * FROM groups")
    fun getAllGroups(): Flow<List<GroupData>>
}