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
import com.dhruv.angular_launcher.accessible_screen.presentation.AccessibleScreenPresentation
import com.dhruv.angular_launcher.interaction_calculation.Trigger
import com.dhruv.angular_launcher.ui.theme.Angular_LauncherTheme
import com.dhruv.angular_launcher.utils.ScreenUtils

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ScreenUtils.pixelDensity = this.resources.displayMetrics.density
        ScreenUtils.screenWidth = this.resources.displayMetrics.widthPixels.toFloat()
        ScreenUtils.screenHeight = this.resources.displayMetrics.heightPixels.toFloat()
        setContent {
            val screenVM: AccessibleScreenVM by remember { mutableStateOf(AccessibleScreenVM()) }

            Angular_LauncherTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AccessibleScreenPresentation(screenVM)
                    Trigger(Modifier.fillMaxSize())
                }
            }
        }
    }
}