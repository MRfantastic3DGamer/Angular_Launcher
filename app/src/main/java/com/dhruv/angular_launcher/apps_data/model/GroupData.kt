package com.dhruv.angular_launcher.apps_data.model

import io.realm.kotlin.ext.realmSetOf
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmSet
import org.mongodb.kbson.ObjectId

class GroupData : RealmObject {
    var _id: ObjectId = ObjectId()
    var name: String = ""
    var iconKey: String = ""
    var apps: RealmSet<AppData> = realmSetOf()
}