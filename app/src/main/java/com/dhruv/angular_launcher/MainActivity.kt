package com.dhruv.angular_launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.dhruv.angular_launcher.accessible_screen.AccessibleScreenVM
import com.dhruv.angular_launcher.accessible_screen.components.fluid_cursor.data.FluidCursorValues
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data.RadialAppNavigatorValues
import com.dhruv.angular_launcher.accessible_screen.components.slider.data.SliderValues
import com.dhruv.angular_launcher.accessible_screen.data.AccessibleScreenValues
import com.dhruv.angular_launcher.accessible_screen.presentation.AccessibleScreen
import com.dhruv.angular_launcher.apps_data.AppsDataVM
import com.dhruv.angular_launcher.apps_data.AppsDataValues
import com.dhruv.angular_launcher.debug.DebugLayerValues
import com.dhruv.angular_launcher.settings_module.prefferences.values.PrefValues
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
        ScreenUtils.pixelDensity = this.resources.displayMetrics.density
        ScreenUtils.screenWidth = this.resources.displayMetrics.widthPixels.toFloat()
        TriggerFunctions.data.sl_width = ScreenUtils.screenWidth
        ScreenUtils.screenHeight = this.resources.displayMetrics.heightPixels.toFloat()
        TriggerFunctions.data.sl_height = ScreenUtils.screenHeight

        setContent {
            var openSettings: Boolean by remember { mutableStateOf(false) }
            val screenVM: AccessibleScreenVM by viewModels<AccessibleScreenVM>()
            val appsDataVM: AppsDataVM by viewModels<AppsDataVM>()
            val settingsVM: SettingsVM by viewModels<SettingsVM>()
            val scope = rememberCoroutineScope()
            LaunchedEffect(Dispatchers.IO){
                async { PrefValues.loadAllValues(context) }.await().apply {
                    AccessibleScreenValues.markPersistentDataDirty()
                    SliderValues.markPersistentDataDirty()
                    RadialAppNavigatorValues.markPersistentDataDirty()
                    FluidCursorValues.markPersistentDataDirty()
                    AppsDataValues.initialize(packageManager)
                }
            }


            Angular_LauncherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DebugLayerValues.addString("number Of apps: ${appsDataVM.apps.collectAsState().value.size}", "apps")
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
                    }
                }
            }
        }
    }
}