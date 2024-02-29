package com.dhruv.angular_launcher

import android.app.WallpaperManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.core.graphics.drawable.toBitmap
import com.dhruv.angular_launcher.accessible_screen.AccessibleScreenVM
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorValues
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data.RadialAppNavigatorValues
import com.dhruv.angular_launcher.accessible_screen.components.slider.data.SliderValues
import com.dhruv.angular_launcher.accessible_screen.data.AccessibleScreenValues
import com.dhruv.angular_launcher.accessible_screen.presentation.AccessibleScreen
import com.dhruv.angular_launcher.core.wallpaper.Wallpaper
import com.dhruv.angular_launcher.core.wallpaper.WallpaperValues
import com.dhruv.angular_launcher.core.database.apps_data.AppsIconsDataValues
import com.dhruv.angular_launcher.core.database.prefferences.values.PrefValues
import com.dhruv.angular_launcher.core.database.room.AppDatabase
import com.dhruv.angular_launcher.debug.DebugLayerVM
import com.dhruv.angular_launcher.interaction_calculation.Trigger
import com.dhruv.angular_launcher.interaction_calculation.TriggerFunctions
import com.dhruv.angular_launcher.settings_screen.SettingsVM
import com.dhruv.angular_launcher.settings_screen.presentation.SettingsScreen
import com.dhruv.angular_launcher.ui.theme.Angular_LauncherTheme
import com.dhruv.angular_launcher.utils.ScreenUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this

        // Retrieve the device's wallpaper
        val wallpaperManager = WallpaperManager.getInstance(this)
        val wallpaperDrawable = wallpaperManager.drawable

        ScreenUtils.pixelDensity = this.resources.displayMetrics.density
        ScreenUtils.screenWidth = this.resources.displayMetrics.widthPixels.toFloat()
        TriggerFunctions.data.sl_width = ScreenUtils.screenWidth
        ScreenUtils.screenHeight = this.resources.displayMetrics.heightPixels.toFloat()
        TriggerFunctions.data.sl_height = ScreenUtils.screenHeight

        WallpaperValues.getWallpaper(context)

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
                    modifier = Modifier,
                    color = Color.Transparent,
                ) {

                    Wallpaper(modifier = Modifier.fillMaxSize())

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
                        if (wallpaperDrawable!=null) {
                            Image(
                                bitmap = wallpaperDrawable
                                    .toBitmap()
                                    .asImageBitmap(),
                                contentDescription = "wallpaper",
                                Modifier
                                    .fillMaxSize(),
                                Alignment.Center,
                                ContentScale.Crop,
                            )
                        }
                        AccessibleScreen(screenVM)
                        Trigger(
                            Modifier
                                .fillMaxSize()
                                .background(Color.Transparent)) {
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
                    }
//                    DebugLayer(vm = debugLayerVM)
                }
            }
        }
    }
}