package com.dhruv.angular_launcher.core.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dhruv.angular_launcher.core.database.room.dao.ThemeDataDao
import com.dhruv.angular_launcher.core.database.room.models.ThemeData


@Database(entities = [ThemeData::class], version = 1)
abstract class ThemeDatabase : RoomDatabase(){
    abstract fun themeDataDao(): ThemeDataDao

    companion object {
        @Volatile
        private var INSTANCE: ThemeDatabase? = null
        @Volatile
        private var VIEWMODEL: ThemeDatabaseVM? = null

        var rdc: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                // do something after database has been created
                // todo: add default themes
                db.beginTransaction()
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                // do something every time database is open
            }
        }

        fun getInstance(context: Context): ThemeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ThemeDatabase::class.java,
                    "app_database",
                )
                    .addCallback(rdc)
                    .fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }

        fun getViewModel(context: Context): ThemeDatabaseVM {
            return (VIEWMODEL ?: synchronized(this){
                val instance = getInstance(context)
                val vm = ThemeDatabaseVM(instance.themeDataDao())
                VIEWMODEL = vm
                vm
            })
        }
    }
}