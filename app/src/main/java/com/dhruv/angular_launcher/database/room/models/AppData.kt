package com.dhruv.angular_launcher.database.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "apps")
data class AppData (
    @PrimaryKey var packageName : String = "",
    var name : String = "",
    var visible : Boolean = true
)