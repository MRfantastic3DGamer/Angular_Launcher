package com.dhruv.angular_launcher.onboarding

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.onboarding.screens.Introduction
import com.dhruv.angular_launcher.onboarding.screens.MediaPermission

@Composable
fun Onboarding(vm: OnboardingVM) {
    Box(modifier = Modifier.fillMaxSize()) {

        val showBackButton = remember { derivedStateOf { vm.currentPage > 0 } }
        val showNextButton = remember { derivedStateOf { vm.currentPage < 3 } }

        AnimatedContent(targetState = vm.currentPage, label = "onboarding") {
            when (it) {
                0 -> {
                    Introduction()
                }
                1 -> {
                    MediaPermission()
                }
                else -> {}
            }
        }

        if (showBackButton.value) {
            Button(
                onClick = vm::goBack,
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                ,
            ) {
                Text(text = "Previous")
            }
        }

        if (showNextButton.value) {
            Button(
                onClick = vm::goForward,
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                ,
            ) {
                Text(text = "next")
            }
        }
    }
}

fun Context.openSettingsForThisApp(){
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}