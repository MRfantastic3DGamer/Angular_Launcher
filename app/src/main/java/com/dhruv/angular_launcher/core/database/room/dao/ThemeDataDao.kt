package com.dhruv.angular_launcher.core.database.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dhruv.angular_launcher.core.database.room.models.ThemeData
import kotlinx.coroutines.flow.Flow

@Dao
interface ThemeDataDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(data: ThemeData)
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(data: ThemeData)
    @Query("DELETE FROM themes WHERE _id == :id")
    suspend fun delete(id: Int)
    @Query("SELECT * FROM themes")
    fun getAllThemes(): Flow<List<ThemeData>>
    @Query("SELECT * FROM themes WHERE _id == :id")
    fun getThemeById(id: Int): ThemeData
}