package com.dhruv.angular_launcher.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dhruv.angular_launcher.database.room.models.AppData
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDataDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(groupData: AppData)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(groupData: AppData)

    @Query("SELECT * FROM apps")
    fun getAllAppsLiveData(): Flow<List<AppData>>
}