package com.dhruv.angular_launcher.settings_screen.presentation.components.theme

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.accessible_screen.components.glsl_art.DefaultShaders
import com.dhruv.angular_launcher.core.database.prefferences.PreferencesManager
import com.dhruv.angular_launcher.core.database.prefferences.values.PrefValues
import com.dhruv.angular_launcher.settings_screen.presentation.components.H1
import com.dhruv.angular_launcher.settings_screen.presentation.components.H2
import com.dhruv.angular_launcher.settings_screen.presentation.components.theme.components.TextureViewer


val MainImageWidth = 200
val ImageWidth = 100

@Composable
fun Theme () {
    val context = LocalContext.current
    val contentResolver = context.contentResolver
    val prefManager = PreferencesManager.getInstance(context)

    var textures = remember { mutableStateListOf<Pair<Int, Int>>() }
    var shaderCode by remember { mutableStateOf(GetLines(PrefValues.t_shader.code)) }
    var bg by remember { mutableStateOf(PrefValues.t_bg_path) }
    var bg1 by remember { mutableStateOf(PrefValues.t_bg_path_1) }
    var bg2 by remember { mutableStateOf(PrefValues.t_bg_path_2) }
    var bg3 by remember { mutableStateOf(PrefValues.t_bg_path_3) }
    var bg4 by remember { mutableStateOf(PrefValues.t_bg_path_4) }
    var bg5 by remember { mutableStateOf(PrefValues.t_bg_path_5) }

    var isEditingShader by remember { mutableStateOf(false) }

    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            println(uri.path)
            contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
        val uris = mutableSetOf(
            uri.toString(),
            bg1,
            bg2,
            bg3,
            bg4,
            bg5,
        ).toList()
        val distinctUris = uris.size
        if (distinctUris > 0) {
            prefManager.saveData("t_bg_path", uris[0])
            prefManager.saveData("t_bg_path_1", uris[0])
            bg = uris[0]
            bg1 = uris[0]
        }
        if (distinctUris > 1) {
            prefManager.saveData("t_bg_path_2", uris[1])
            bg2 = uris[1]
        }
        if (distinctUris > 2) {
            prefManager.saveData("t_bg_path_3", uris[2])
            bg3 = uris[2]
        }
        if (distinctUris > 3) {
            prefManager.saveData("t_bg_path_4", uris[3])
            bg4 = uris[3]
        }
        if (distinctUris > 4) {
            prefManager.saveData("t_bg_path_5", uris[4])
            bg5 = uris[4]
        }
    }
    fun saveShader(){
        // TODO
    }

    Column {

        H2(text = "Sample shaders")
        Spacer(modifier = Modifier.height(20.dp))
        LazyVerticalGrid(columns = GridCells.Adaptive(100.dp)) {
            items(DefaultShaders) {
                Button(onClick = {  }) {
                    Text(text = it.name)
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        AnimatedVisibility(visible = !isEditingShader) {
            Button(onClick = {
                saveShader()
                isEditingShader = true
            }) {
                H2(text = "start editing")
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        AnimatedVisibility(visible = isEditingShader) {
            ShaderEditor(
                lines = shaderCode,
                shaderTap = { image, stream ->
                    val temp = Pair(image, stream)
                    if (textures.contains(temp)) textures.remove(temp)
                    else textures.add(temp)
                },
                onChangeLine = { i, s ->
                    shaderCode = shaderCode.toMutableList().apply { set(i, s) }
                },
                onDelLine = { i ->
                    shaderCode = shaderCode.toMutableList().apply { removeAt(i) }
                },
                onNewLine = { i ->
                    shaderCode = shaderCode.toMutableList().apply { add(i, "") }
                },
                onFinishEditing = {

//                    prefManager.saveData("t_shader", ShaderData("custom shader", textures, createPera(shaderCode)))
                },
                copy = {
                    val shaderText = createPera(shaderCode)
                    val clipboard =
                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Copied Text", shaderText)
                    clipboard.setPrimaryClip(clip)
                },
                closeEditor = {
                    isEditingShader = false
                }
            )
        }
    }

}

fun GetLines(pera: String): MutableList<String> {
    val res = pera.split("\n").toMutableList()
    return res
}
fun createPera(lines: List<String>): String{
    var res = ""
    lines.forEachIndexed{i, it ->
        res += "${ if (i != 0) "\n" else ""}$it"
    }
    return res
}

@Composable
fun DefaultShaderOption(name: String, onSelect: ()->Unit) {
    Text(
        text = name,
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(100))
            .background(Color(1.0f, 0.922f, 0.231f, 1.0f))
            .clickable {
                onSelect()
            }
    )
}

@Composable
fun ShaderEditor(
    shaderTap: (Int, Int) -> Unit,
    onNewLine: (Int) -> Unit,
    onDelLine: (Int) -> Unit,
    lines: List<String>,
    onFinishEditing: () -> Unit,
    copy: () -> Unit,
    closeEditor: () -> Unit,
    onChangeLine: (Int, String) -> Unit,
) {
    val context = LocalContext.current
    val resources = context.resources

    /**
     * l1 - compressed texture
     * p.start - image
     * p.end - stream
     */
    val selectedTextures by remember { mutableStateOf(setOf<Pair<Int, Int>>()) }

    Column {
        LazyColumn {

            TextureViewer(selectedTextures = selectedTextures, resources = resources, onSelect = shaderTap)

            // TODO texture compressing module

            item {
                H1(text = "fragment code Editor")
                H2(text = "this code will run in parallel for each pixel")
            }

            items(lines.size) { i ->

                val isComment = lines[i].length >= 2 && lines[0] == "/" && lines[1] == "/"

                Row (
                    Modifier,
                    Arrangement.Start,
                    Alignment.CenterVertically
                ) {
                    TextField(
                        value = lines[i],
                        onValueChange = {
                            onChangeLine(i, it)
                        },
                        Modifier
                            .fillMaxWidth()
                            .background(Color.Black),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Black,
                            unfocusedContainerColor = Color.Black,
                            disabledContainerColor = Color.Black,
                            cursorColor = Color.Cyan,
                            focusedTextColor = Color.White,
                            disabledTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                        ),
                        trailingIcon = {
                            Column(
                                modifier = Modifier
                                    .width(15.dp),
                                verticalArrangement = Arrangement.SpaceEvenly,
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "delete",
                                    tint = Color.Red,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable {
                                            onDelLine(i)
                                        }
                                )
                                Divider()
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "add",
                                    tint = Color.Green,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable {
                                            onNewLine(i + 1)
                                        }
                                )
                            }
                        },
                        label = { Text(text = (i + 1).toString())},
                        textStyle = when (isComment) {
                            true -> TextStyle(
                                fontSize = TextUnit(8f, TextUnitType.Sp),
                                color = Color.Green
                            )
                            false -> TextStyle(
                                fontSize = TextUnit(10f, TextUnitType.Sp)
                            )
                        },
                    )
                }
            }
            item {
                Row (
                    Modifier.fillMaxWidth(),
                    Arrangement.SpaceEvenly,
                    Alignment.CenterVertically
                ){
                    Button(
                        onClick = { closeEditor() },
                    ) {
                        H2(text = "close")
                    }
                    Button(
                        onClick = { copy() },
                    ) {
                        H2(text = "copy")
                    }
                    Button(
                        onClick = { onFinishEditing() },
                    ) {
                        H2(text = "save")
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(500.dp))
            }
        }
    }
}

@Preview
@Composable
private fun EditorPrev() {
    val lines = mutableListOf(
    "float value = 1./distance(gl_FragCoord.xy, u_mouse) * 1.5;",
    "vec2 t_pos = vec2(0., 0.);",
    "float t_dist = 100000.;",
    "",
    "for(int i=0;i<com.dhruv.angular_launcher.accessible_screen.components.glsl_art.MAX_ICONS;++i)",
    "{",
    "    if (u_positions_X[i] == -10000.)",
    "        break;",
    "    t_pos = vec2(u_positions_X[i], u_positions_Y[i]);",
    "    t_dist = min(t_dist, distance(gl_FragCoord.xy, t_pos));",
    "}",
    )
    ShaderEditor({_,_-> }, { }, { }, lines = lines, {}, {}, {}) { _, _ -> }
}