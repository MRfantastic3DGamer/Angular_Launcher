package com.dhruv.angular_launcher.database.realm

//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.RadialAppNavigationFunctions
//import com.dhruv.angular_launcher.apps_data.MyApplication
//import com.dhruv.angular_launcher.settings_module.room.models.AppData
//import com.dhruv.angular_launcher.settings_module.room.models.GroupData
//import io.realm.kotlin.UpdatePolicy
//import io.realm.kotlin.ext.query
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.launch
//
//class RealmVM : ViewModel() {
//
//    val realm = MyApplication.realm
//
//    val apps = realm
//        .query<AppData>()
//        .asFlow()
//        .map { res -> res.list.toList() }
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
//    val groups = realm
//        .query<GroupData>()
//        .asFlow()
//        .map { res -> res.list.toList() }
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
//
//    val appsByChar: StateFlow<Map<String, List<String>>> = realm
//        .query<AppData>()
//        .asFlow()
//        .map { res ->
//            val packagesPerChar = mutableMapOf<String, MutableList<String> >()
//            res.list.forEach {  app ->
//                val firstChar = RadialAppNavigationFunctions.getFirstChar(app.name)
//                if (firstChar in packagesPerChar)
//                    packagesPerChar[firstChar]!!.add(app.packageName)
//                else
//                    packagesPerChar[firstChar] = mutableListOf(app.packageName)
//            }
//            packagesPerChar.toMap()
//        }
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyMap())
//
//    val alphabets = realm
//        .query<AppData>()
//        .asFlow()
//        .map { res ->
//            res.list.toList().map {
//                val f = it.name.first()
//                if (f.isLetter()) f.uppercase()
//                else "#"
//            }.sorted().toSet().toList()
//        }
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
//
//    fun upsertApp (app: AppData, dontUpdateData: Boolean = false){
//        viewModelScope.launch (Dispatchers.IO) {
//            realm.write {
//                val findPrev = this.query(
//                    AppData::class,
//                    "packageName = $0",
//                    app.packageName
//                ).find()
//                println("${app.name} present : ${!findPrev.isEmpty()}")
//                if (!findPrev.isEmpty()) {
//                    if (!dontUpdateData){
//                        val latestApp = findLatest(app)!!
//                        latestApp.apply {
//                            this.name = app.name
//                            this.packageName = app.name
//                        }
//                        copyToRealm(latestApp, UpdatePolicy.ALL)
//                    }
//                }
//                else{
//                    copyToRealm(app)
//                }
//            }
//        }
//    }
//
//    fun upsertGroup (group: GroupData){
//        viewModelScope.launch (Dispatchers.IO) {
//            realm.write {
//                val latestGroup = findLatest(group)
//
//                if (latestGroup != null){
//                    latestGroup.apply {
//                        name = group.name
//                        apps.clear()
//                        val apps = group.apps.toList().forEach{
//                            apps.add(findLatest(it)!!)
//                        }
//                    }
//                    copyToRealm(latestGroup, UpdatePolicy.ALL)
//                }
//                else{
//                    copyToRealm(group, UpdatePolicy.ALL)
//                }
//            }
//        }
//    }
//
//    companion object {
//        private lateinit var instance: RealmVM
//
//        fun getInstance (): RealmVM {
//            instance = RealmVM()
//            return instance
//        }
//    }
//}