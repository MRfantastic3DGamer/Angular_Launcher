package com.dhruv.angular_launcher.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dhruv.angular_launcher.database.room.models.GroupData
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(groupData: GroupData)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(groupData: GroupData)

    @Query("SELECT * FROM groups")
    fun getAllGroups(): Flow<List<GroupData>>
}