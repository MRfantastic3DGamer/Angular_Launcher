package com.dhruv.angular_launcher.settings_screen.presentation.components.theme

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.dhruv.angular_launcher.database.prefferences.PreferencesManager
import com.dhruv.angular_launcher.utils.BitmapFromURI
import com.dhruv.angular_launcher.utils.encodeToBase64

@Composable
fun Theme () {
    ImageSelector()
}
@Composable
fun ImageSelector() {
    val context = LocalContext.current
    val prefManager = remember { PreferencesManager.getInstance(context) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { result ->
        if (result != null) {
            println("result type : ${result.path}")
            val bitmap = BitmapFromURI(context.contentResolver, result)
            if (bitmap != null) {
                prefManager.saveData("wallPaper", encodeToBase64(bitmap) ?: "")
            }
        }
    }

    Button(onClick = {
        launcher.launch("image/*")
    }) {
        Text(text = "select image")
    }
}