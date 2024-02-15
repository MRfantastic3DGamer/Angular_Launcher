package com.dhruv.angular_launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.dhruv.angular_launcher.accessible_screen.AccessibleScreenVM
import com.dhruv.angular_launcher.accessible_screen.components.radial_app_navigator.data.RadialAppNavigatorValues
import com.dhruv.angular_launcher.accessible_screen.components.slider.data.SliderValues
import com.dhruv.angular_launcher.accessible_screen.data.ScreenValues
import com.dhruv.angular_launcher.accessible_screen.presentation.AccessibleScreen
import com.dhruv.angular_launcher.settings_module.prefferences.values.PrefValues
import com.dhruv.angular_launcher.interaction_calculation.Trigger
import com.dhruv.angular_launcher.interaction_calculation.TriggerFunctions
import com.dhruv.angular_launcher.ui.theme.Angular_LauncherTheme
import com.dhruv.angular_launcher.utils.ScreenUtils

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ScreenUtils.pixelDensity = this.resources.displayMetrics.density
        ScreenUtils.screenWidth = this.resources.displayMetrics.widthPixels.toFloat()
        TriggerFunctions.data.sl_width = ScreenUtils.screenWidth
        ScreenUtils.screenHeight = this.resources.displayMetrics.heightPixels.toFloat()
        TriggerFunctions.data.sl_height = ScreenUtils.screenHeight

        PrefValues.getAllValues(this)
        ScreenValues.markPersistentDataDirty()
        SliderValues.markPersistentDataDirty()
        RadialAppNavigatorValues.markPersistentDataDirty()
        setContent {
            val screenVM: AccessibleScreenVM by remember { mutableStateOf(AccessibleScreenVM()) }

            Angular_LauncherTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    if (SettingsValues.GetIsSettingsOpened.value == true){
//                        SettingsScreen()
//                    }
//                    else{
                        AccessibleScreen(screenVM)
                        Trigger(Modifier.fillMaxSize())
//                    }
                }
            }
        }
    }
}