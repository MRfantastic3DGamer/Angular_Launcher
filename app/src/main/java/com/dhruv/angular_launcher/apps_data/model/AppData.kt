package com.dhruv.angular_launcher.apps_data.model

import io.realm.kotlin.ext.backlinks
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class AppData : RealmObject {
    @PrimaryKey var packageName : String = ""
    var name:String = ""
    val joinedGroups: RealmResults<GroupData> by backlinks(GroupData::apps)
}