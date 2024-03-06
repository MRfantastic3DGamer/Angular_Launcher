package com.dhruv.angular_launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalHapticFeedback
import com.dhruv.angular_launcher.accessible_screen.AccessibleScreenVM
import com.dhruv.angular_launcher.accessible_screen.presentation.AccessibleScreen
import com.dhruv.angular_launcher.core.database.apps_data.AppsIconsDataValues
import com.dhruv.angular_launcher.core.database.room.AppDatabase
import com.dhruv.angular_launcher.debug.DebugLayer
import com.dhruv.angular_launcher.debug.DebugLayerVM
import com.dhruv.angular_launcher.onboarding.Onboarding
import com.dhruv.angular_launcher.onboarding.OnboardingVM
import com.dhruv.angular_launcher.settings_screen.SettingsVM
import com.dhruv.angular_launcher.ui.theme.Angular_LauncherTheme
import com.dhruv.angular_launcher.utils.ScreenUtils

class MainActivity : ComponentActivity() {

//    override fun onBackPressed() {
//        super.onBackPressed()
//        myFunction()
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this

        ScreenUtils.pixelDensity = this.resources.displayMetrics.density
        ScreenUtils.screenWidth = this.resources.displayMetrics.widthPixels.toFloat()
        ScreenUtils.screenHeight = this.resources.displayMetrics.heightPixels.toFloat()


        setContent {
            val haptic = LocalHapticFeedback.current

//            val scope = rememberCoroutineScope()
            val debugLayerVM by remember { mutableStateOf(DebugLayerVM()) }

            val settingsVM by remember { mutableStateOf(SettingsVM()) }
            val onboardingVM by remember { mutableStateOf(OnboardingVM(endOnboarding = {
                AppsIconsDataValues.initialize(packageManager, AppDatabase.getInstance(context).appDataDao())
                settingsVM.exitSettings(context)
            })) }
            val screenVM: AccessibleScreenVM by viewModels<AccessibleScreenVM>()
            AppDatabase.getInstance(context)

            Angular_LauncherTheme {
                Surface(
                    modifier = Modifier,
                    color = Color.Transparent,
                ) {
                    when (onboardingVM.onBoardingComplete) {
                        true -> {
                            AccessibleScreen(mainScreenVM = screenVM, settingsVM = settingsVM)
                        }
                        false -> {
                            Onboarding(vm = onboardingVM)
                        }
                    }
                    DebugLayer(vm = debugLayerVM)
                }
            }
        }
    }
}