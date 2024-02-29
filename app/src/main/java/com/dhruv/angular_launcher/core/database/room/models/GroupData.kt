package com.dhruv.angular_launcher.core.database.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class GroupData(
    @PrimaryKey(autoGenerate = true) var _id: Int = 0,
    var name: String = "",
    var iconKey: String = ""
)