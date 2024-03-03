package com.dhruv.angular_launcher.onboarding.screens

import android.Manifest.permission.MANAGE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.dhruv.angular_launcher.onboarding.openSettingsForThisApp

@Composable
fun MediaPermission() {
    val context = LocalContext.current

    val permissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            if (!it){
                context.openSettingsForThisApp()
            }
        }
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(80.dp),
        Alignment.Center
    ) {
        Column {
            Text(
                text = "We will need the permission to access media to use wallpaper",
                color = Color.White
            )

            val haveImagesPermission = ContextCompat.checkSelfPermission(
                context,
                MANAGE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

            Spacer(modifier = Modifier.height(100.dp))

            if (haveImagesPermission){
                Text(text = "permission granted")
            }
            else{
                Button(onClick = {
                    permissionResultLauncher.launch(
                        MANAGE_EXTERNAL_STORAGE
                    )
                }) {
                    Text(text = "Allow")
                }
            }
        }
    }
}

@Preview
@Composable
private fun media() {
    MediaPermission()
}