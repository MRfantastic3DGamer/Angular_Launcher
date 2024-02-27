package com.dhruv.angular_launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import com.dhruv.angular_launcher.accessible_screen.AccessibleScreenVM
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorValues
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data.RadialAppNavigatorValues
import com.dhruv.angular_launcher.accessible_screen.components.slider.data.SliderValues
import com.dhruv.angular_launcher.accessible_screen.data.AccessibleScreenValues
import com.dhruv.angular_launcher.accessible_screen.presentation.AccessibleScreen
import com.dhruv.angular_launcher.database.apps_data.AppsIconsDataValues
import com.dhruv.angular_launcher.database.prefferences.values.PrefValues
import com.dhruv.angular_launcher.database.room.AppDatabase
import com.dhruv.angular_launcher.debug.DebugLayerVM
import com.dhruv.angular_launcher.interaction_calculation.Trigger
import com.dhruv.angular_launcher.interaction_calculation.TriggerFunctions
import com.dhruv.angular_launcher.settings_screen.SettingsVM
import com.dhruv.angular_launcher.settings_screen.presentation.SettingsScreen
import com.dhruv.angular_launcher.ui.theme.Angular_LauncherTheme
import com.dhruv.angular_launcher.utils.ScreenUtils
import com.dhruv.angular_launcher.utils.decodeBase64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this
        ScreenUtils.pixelDensity = this.resources.displayMetrics.density
        ScreenUtils.screenWidth = this.resources.displayMetrics.widthPixels.toFloat()
        TriggerFunctions.data.sl_width = ScreenUtils.screenWidth
        ScreenUtils.screenHeight = this.resources.displayMetrics.heightPixels.toFloat()
        TriggerFunctions.data.sl_height = ScreenUtils.screenHeight

        setContent {
            val debugLayerVM by remember { mutableStateOf(DebugLayerVM()) }
            var openSettings: Boolean by remember { mutableStateOf(false) }
            val screenVM: AccessibleScreenVM by viewModels<AccessibleScreenVM>()
            val settingsVM: SettingsVM by viewModels<SettingsVM>()
            val database = AppDatabase.getInstance(context)
            val scope = rememberCoroutineScope()

            LaunchedEffect(Dispatchers.IO){
                async { PrefValues.loadAllValues(context) }.await().apply {
                    AppsIconsDataValues.initialize(packageManager, database.appDataDao())
                    AccessibleScreenValues.markPersistentDataDirty()
                    SliderValues.markPersistentDataDirty()
                    RadialAppNavigatorValues.markPersistentDataDirty()
                    FluidCursorValues.markPersistentDataDirty()
                }
            }


            Angular_LauncherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (openSettings){
                        SettingsScreen(settingsVM){
                            scope.launch {
                                async { settingsVM.save(context) }.await().also {
                                    async { PrefValues.loadAllValues(context) }.await().also {
                                        AccessibleScreenValues.markPersistentDataDirty()
                                        SliderValues.markPersistentDataDirty()
                                        RadialAppNavigatorValues.markPersistentDataDirty()
                                        FluidCursorValues.markPersistentDataDirty()
                                        openSettings = false
                                    }
                                }
                            }
                        }
                    }
                    else{
                        AccessibleScreen(screenVM)
                        Trigger(Modifier.fillMaxSize()) {
                            scope.launch {
                                async { settingsVM.save(context) }.await().also {
                                    async { PrefValues.loadAllValues(context) }.await().also {
                                        AccessibleScreenValues.markPersistentDataDirty()
                                        SliderValues.markPersistentDataDirty()
                                        RadialAppNavigatorValues.markPersistentDataDirty()
                                        FluidCursorValues.markPersistentDataDirty()
                                        openSettings = true
                                    }
                                }
                            }
                        }
                        if (PrefValues.wallpaper != ""){
                            val bitmap = decodeBase64(PrefValues.wallpaper)
                            if (bitmap != null) {
                                Image(bitmap = bitmap.asImageBitmap(), contentDescription = "wallpaper")
                            }
                            else{
                                println("couldn't read image")
                            }
                        }
                    }
//                    DebugLayer(vm = debugLayerVM)
                }
            }
        }
    }
}