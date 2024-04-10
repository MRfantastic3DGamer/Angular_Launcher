package com.dhruv.angular_launcher.settings_screen.presentation.components.shader

import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.core.database.room.ThemeDatabase
import com.dhruv.angular_launcher.core.database.room.models.getShader
import com.dhruv.angular_launcher.core.database.room.models.setShader
import com.dhruv.angular_launcher.settings_screen.ThemeUIEvent
import com.dhruv.angular_launcher.settings_screen.presentation.components.H1
import com.dhruv.angular_launcher.settings_screen.presentation.components.H2
import com.dhruv.angular_launcher.settings_screen.presentation.components.H3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Shader() {
    val context = LocalContext.current
    val resources = context.resources
    val contentResolver = context.contentResolver
    var texturesViewerOpen by remember { mutableStateOf(false) }
    var requirementsViewerOpen by remember { mutableStateOf(false) }
    val themeVM = remember { ThemeDatabase.getViewModel(context) }
    val currentTheme = themeVM.currTheme
    var name by remember { mutableStateOf(TextFieldValue(currentTheme.getShader().name)) }
    var code by remember { mutableStateOf(TextFieldValue(currentTheme.getShader().code)) }
    var resourcesAsked by remember { mutableStateOf(currentTheme.getShader().resourcesAsked) }
    var textures by remember { mutableStateOf(currentTheme.getShader().textures.toSet()) }

    val selectImage = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { data ->
        data?.let { uri ->
            val new = textures.toMutableSet()
            new.add(uri.toString())
            textures = new
        }
    }

    Box (
        Modifier.fillMaxSize()
    ) {
        LazyColumn {
            val scope = this
            item {
                H1(text = "edit shader")
            }

            item {
                TextField(value = name, onValueChange = { name = it }, Modifier, label = { Text(text = "name of the shader") })
            }

            item {
                Button(onClick = {
                    selectImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }) {
                    H2(text = "pick texture")
                }
            }

            item {
                LazyRow {
                    items(textures.toList()){ uri ->
                        val source = ImageDecoder.createSource(contentResolver, Uri.parse(uri))
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        Image(bitmap = bitmap.asImageBitmap(), contentDescription = null, Modifier.size(300.dp, 300.dp))
                    }
                }
            }

//            item {
//                Button(onClick = { texturesViewerOpen = true }) {
//                    H2(text = "backed noise maps")
//                }
//            }



            item {
                Button(onClick = { requirementsViewerOpen = true }) {
                    H2(text = "equip resources")
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }

            item {
                CodeEditor(
                    code = code,
                    onChange = {
                        code = it
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(500.dp)) }
        }

        Button(
            onClick = {
                textures.forEach {
                    val uri = Uri.parse(it)
                    contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                val newShader = currentTheme.getShader().copy(
                    name = name.text,
                    resourcesAsked = resourcesAsked,
                    textures = textures.toList(),
                    code = code.text,
                )
                themeVM.onUIInput(
                    ThemeUIEvent.UpdateCurrentTheme(
                        contentResolver,
                        themeVM.currTheme.setShader( newShader )
                    )
                )
                println(newShader.toString())
            },
            Modifier
                .align(Alignment.BottomEnd)
        ) {
            H3(text = "save shader")
        }

        when{
            texturesViewerOpen -> AlertDialog(onDismissRequest = { texturesViewerOpen = false }) {
                LazyColumn {
                    textureViewer(
                        allMaps = getAllAvailableDefaultMaps(),
                        selectedTextures = setOf(),
                        resources = resources,
                        onSelect = { i, j -> }
                    )
                }
            }

            requirementsViewerOpen -> AlertDialog(onDismissRequest = { requirementsViewerOpen = false }) {
                LazyColumn {
                    requirementsViewer(
                        selected = resourcesAsked,
                        onSelect = {
                            val mutSet = resourcesAsked.toMutableSet()
                            if (resourcesAsked.contains(it.name)) mutSet.remove(it.name)
                            else mutSet.add(it.name)
                            resourcesAsked = mutSet
                        }
                    )
                }
            }
        }
    }
}