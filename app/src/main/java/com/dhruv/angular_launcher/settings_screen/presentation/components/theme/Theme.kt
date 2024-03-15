package com.dhruv.angular_launcher.settings_screen.presentation.components.theme

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.core.database.prefferences.PreferencesManager
import com.dhruv.angular_launcher.core.database.prefferences.values.PrefValues


val MainImageWidth = 200
val ImageWidth = 100

@Composable
fun Theme () {
    val context = LocalContext.current
    val contentResolver = context.contentResolver
    val prefManager = PreferencesManager.getInstance(context)

    var shader by remember { mutableStateOf( GetLines(PrefValues.t_shader)) }
    var bg by remember { mutableStateOf(PrefValues.t_bg_path) }
    var bg1 by remember { mutableStateOf(PrefValues.t_bg_path_1) }
    var bg2 by remember { mutableStateOf(PrefValues.t_bg_path_2) }
    var bg3 by remember { mutableStateOf(PrefValues.t_bg_path_3) }
    var bg4 by remember { mutableStateOf(PrefValues.t_bg_path_4) }
    var bg5 by remember { mutableStateOf(PrefValues.t_bg_path_5) }

    ShaderEditor(
        lines = shader,
        onChangeLine = { i, s ->
            shader = shader.toMutableList().apply { set(i, s) }
        },
        onDelLine = { i ->
            shader = shader.toMutableList().apply { removeAt(i) }
        },
        onNewLine = { i ->
            shader = shader.toMutableList().apply { add(i, "") }
        },
        onFinishEditing = {
            prefManager.saveData("t_shader", createPera(shader))
        }
    )

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

//    Column {
//
//        ImagePrev(uriString = bg, contentResolver = contentResolver, MainImageWidth) {}
//
//        LazyVerticalGrid(columns = GridCells.Adaptive(100.dp)) {
//            item(key = "new") {
//                IconButton(
//                    onClick = { picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
//                    modifier = Modifier.size(ImageWidth.dp, (ImageWidth * ScreenUtils.HPerW).dp)
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Add,
//                        contentDescription = "add",
//                        Modifier
//                            .size(10.dp)
//                            .size(20.dp),
//                        tint = Color.White
//                    )
//                }
//            }
//            item(key = "1") {
//                ImagePrev(uriString = bg1, contentResolver, ImageWidth) {
//                    bg = bg1
//                    prefManager.saveData("t_bg_path", bg)
//                }
//            }
//            item(key = "2") {
//                ImagePrev(uriString = bg2, contentResolver, ImageWidth) {
//                    bg = bg2
//                    prefManager.saveData("t_bg_path", bg)
//                }
//            }
//            item(key = "3") {
//                ImagePrev(uriString = bg3, contentResolver, ImageWidth) {
//                    bg = bg3
//                    prefManager.saveData("t_bg_path", bg)
//                }
//            }
//            item(key = "4") {
//                ImagePrev(uriString = bg4, contentResolver, ImageWidth) {
//                    bg = bg4
//                    prefManager.saveData("t_bg_path", bg)
//                }
//            }
//            item(key = "5") {
//                ImagePrev(uriString = bg5, contentResolver, ImageWidth) {
//                    bg = bg5
//                    prefManager.saveData("t_bg_path", bg)
//                }
//            }
//        }
//    }
}

//@Composable
//fun ImagePrev(uriString: String, contentResolver: ContentResolver, width: Int, onTap: ()->Unit) {
//    val uri = Uri.parse(uriString)
//    val bitmap = BitmapFromURI(uri = uri, contentResolver = contentResolver)?.asImageBitmap()
//    if (bitmap != null) {
//        Image(
//            bitmap = bitmap,
//            contentDescription = "bg-image",
//            Modifier
//                .size(width.dp, (width * ScreenUtils.HPerW).dp)
//                .padding(5.dp)
//                .clip(RoundedCornerShape(10.dp))
//                .clickable { onTap() },
//            Alignment.Center,
//            ContentScale.FillBounds
//        )
//    }
//}

fun GetLines(pera: String): MutableList<String> {
    val res = pera.split("\n").toMutableList()
    return res
}
fun createPera(lines: List<String>): String{
    var res = ""
    lines.forEach { res += "\n$it" }
    return res
}

@Composable
fun ShaderEditor(
    lines: List<String>,
    onNewLine: (Int) -> Unit,
    onDelLine: (Int) -> Unit,
    onChangeLine: (Int, String) -> Unit,
    onFinishEditing: ()->Unit,
) {
    LazyColumn {
        item {
            Text(text = "Editor")
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
                    onValueChange = { onChangeLine(i, it) },
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
                                        onFinishEditing()
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
                                        onFinishEditing()
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
                    keyboardActions = KeyboardActions(onAny = { onFinishEditing() })
                )
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
    "for(int i=0;i<MAX_ICONS;++i)",
    "{",
    "    if (u_positions_X[i] == -10000.)",
    "        break;",
    "    t_pos = vec2(u_positions_X[i], u_positions_Y[i]);",
    "    t_dist = min(t_dist, distance(gl_FragCoord.xy, t_pos));",
    "}",
    "value += 1./t_dist;",
    "",
    "float radius = 1./50.0;",
    "",
    "float insideCircle = step(value, radius);",
    "",
    "vec4 color = insideCircle * vec4(0.0, 0.0, 0.0, 0.0);",
    "color += (1.0 - insideCircle) * vec4(1.0, 1.0, 1.0, 1.0);",
    "",
    "gl_FragColor = color;",
    )
    ShaderEditor(lines = lines, {i->}, {i->}, {i,s -> }, {})
}