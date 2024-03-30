package com.dhruv.angular_launcher.core.database.room.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.dhruv.angular_launcher.R

@Entity(tableName = "apps")
data class AppData (
    @PrimaryKey var packageName : String = "",
    var name : String = "",
    var visible : Boolean = true
)

@Entity(tableName = "groups")
data class GroupData(
    @PrimaryKey(autoGenerate = true) var _id: Int = 0,
    var name: String = "",
    var iconKey: String = R.drawable.group_social.toString()
)

// Junction table to represent the many-to-many relationship
@Entity(tableName = "group_app_cross_ref",
    primaryKeys = ["groupId", "appId"],
    foreignKeys = [
        ForeignKey(
            entity = GroupData::class,
            parentColumns = ["_id"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AppData::class,
            parentColumns = ["packageName"],
            childColumns = ["appId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GroupAppCrossRef(
    val groupId: Int,
    val appId: String
)