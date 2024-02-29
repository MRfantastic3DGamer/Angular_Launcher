package com.dhruv.angular_launcher.core.database.room.models

import androidx.room.Entity
import androidx.room.ForeignKey

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