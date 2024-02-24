package com.dhruv.angular_launcher.apps_data

import android.app.Application
import com.dhruv.angular_launcher.apps_data.model.AppData
import com.dhruv.angular_launcher.apps_data.model.GroupData
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class MyApplication: Application() {

    companion object{
        lateinit var realm: Realm
    }

    override fun onCreate() {
        super.onCreate()
        realm = Realm.open(
            configuration = RealmConfiguration.create(
                schema = setOf(
                    AppData::class,
                    GroupData::class,
                )
            )
        )
    }
}