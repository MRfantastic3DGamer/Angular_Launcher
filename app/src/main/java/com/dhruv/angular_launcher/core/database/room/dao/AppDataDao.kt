package com.dhruv.angular_launcher.core.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dhruv.angular_launcher.core.database.room.models.AppData
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDataDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(groupData: AppData)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(groupData: AppData)

    @Query("SELECT * FROM apps ORDER BY name")
    fun getAllAppsLiveData(): Flow<List<AppData>>

    @Query("SELECT * FROM apps WHERE visible = 1 ORDER BY name")
    fun getAllVisibleApps(): Flow<List<AppData>>

    @Query("DELETE FROM apps WHERE apps.name NOT IN (:installedApps)")
    fun deleteExtraApps(installedApps: List<String>)
}