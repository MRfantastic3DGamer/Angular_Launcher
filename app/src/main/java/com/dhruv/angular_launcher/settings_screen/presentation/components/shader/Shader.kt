package com.dhruv.angular_launcher.settings_screen.presentation.components.shader

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.core.database.room.ThemeDatabase
import com.dhruv.angular_launcher.core.database.room.models.getShader
import com.dhruv.angular_launcher.core.database.room.models.setShader
import com.dhruv.angular_launcher.settings_screen.presentation.components.H1
import com.dhruv.angular_launcher.settings_screen.presentation.components.H2
import com.dhruv.angular_launcher.settings_screen.ThemeUIEvent

@Composable
fun Shader() {
    val context = LocalContext.current
    val resources = context.resources
    var open by remember { mutableStateOf(false) }
    val themeVM = remember { ThemeDatabase.getViewModel(context) }

//    val picker = rememberLauncherForActivityResult(
//        ActivityResultContracts.PickVisualMedia()
//    ) { uri ->
//        if (uri != null) {
//            println(uri.path)
//            contentResolver.takePersistableUriPermission(
//                uri,
//                Intent.FLAG_GRANT_READ_URI_PERMISSION
//            )
//        }
//        val uris = mutableSetOf(
//            uri.toString(),
//            bg1,
//            bg2,
//            bg3,
//            bg4,
//            bg5,
//        ).toList()
//        val distinctUris = uris.size
//        if (distinctUris > 0) {
//            prefManager.saveData("t_bg_path", uris[0])
//            prefManager.saveData("t_bg_path_1", uris[0])
//            bg = uris[0]
//            bg1 = uris[0]
//        }
//        if (distinctUris > 1) {
//            prefManager.saveData("t_bg_path_2", uris[1])
//            bg2 = uris[1]
//        }
//        if (distinctUris > 2) {
//            prefManager.saveData("t_bg_path_3", uris[2])
//            bg3 = uris[2]
//        }
//        if (distinctUris > 3) {
//            prefManager.saveData("t_bg_path_4", uris[3])
//            bg4 = uris[3]
//        }
//        if (distinctUris > 4) {
//            prefManager.saveData("t_bg_path_5", uris[4])
//            bg5 = uris[4]
//        }
//    }

    LazyColumn {
        val scope = this
        item {
            H1(text = "edit shader")
        }

        item {
            Button(onClick = { open = !open }) {
                H2(text = "backed noise maps")
            }
        }

        textureViewer(
            visibility = open,
            allMaps = getAllAvailableDefaultMaps(),
            selectedTextures = setOf(),
            resources = resources,
            onSelect = { i, j -> }
        )

        item { Spacer(modifier = Modifier.height(20.dp)) }

        item {
            var tfv by remember { mutableStateOf(TextFieldValue(themeVM.currTheme.getShader().code)) }
            CodeEditor(
                onSave = {
                    themeVM.onUIInput(ThemeUIEvent.UpdateCurrentTheme(resources, themeVM.currTheme.setShader( themeVM.currTheme.getShader().copy(code = tfv.text) )))
                },
                code = tfv,
                onChange = {
                    tfv = it
                }
            )
        }
    }
}