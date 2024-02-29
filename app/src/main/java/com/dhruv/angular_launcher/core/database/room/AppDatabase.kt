package com.dhruv.angular_launcher.core.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dhruv.angular_launcher.core.database.room.dao.AppDataDao
import com.dhruv.angular_launcher.core.database.room.dao.GroupAppCrossRefDao
import com.dhruv.angular_launcher.core.database.room.dao.GroupDataDao
import com.dhruv.angular_launcher.core.database.room.models.AppData
import com.dhruv.angular_launcher.core.database.room.models.GroupAppCrossRef
import com.dhruv.angular_launcher.core.database.room.models.GroupData

@Database(entities = [AppData::class, GroupData::class, GroupAppCrossRef::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDataDao(): AppDataDao
    abstract fun groupDataDao(): GroupDataDao
    abstract fun groupAppCrossRefDao(): GroupAppCrossRefDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        @Volatile
        private var VIEWMODEL: AppDatabaseVM? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }

        fun getViewModel(context: Context): AppDatabaseVM {
            return (VIEWMODEL ?: synchronized(this){
                val instance = getInstance(context)
                val vm = AppDatabaseVM(instance.appDataDao(), instance.groupDataDao(), instance.groupAppCrossRefDao())
                VIEWMODEL = vm
                vm
            })
        }
    }
}