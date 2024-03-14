package com.dhruv.angular_launcher.settings_screen.presentation.components.theme

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.core.database.prefferences.PreferencesManager
import com.dhruv.angular_launcher.core.database.prefferences.values.PrefValues
import com.dhruv.angular_launcher.utils.BitmapFromURI
import com.dhruv.angular_launcher.utils.ScreenUtils


val MainImageWidth = 200
val ImageWidth = 100

@Composable
fun Theme () {
    val context = LocalContext.current
    val contentResolver = context.contentResolver
    val prefManager = PreferencesManager.getInstance(context)

    var bg by remember { mutableStateOf(PrefValues.t_bg_path) }
    var bg1 by remember { mutableStateOf(PrefValues.t_bg_path_1) }
    var bg2 by remember { mutableStateOf(PrefValues.t_bg_path_2) }
    var bg3 by remember { mutableStateOf(PrefValues.t_bg_path_3) }
    var bg4 by remember { mutableStateOf(PrefValues.t_bg_path_4) }
    var bg5 by remember { mutableStateOf(PrefValues.t_bg_path_5) }

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

    Column {

//        ImagePrev(uriString = bg, contentResolver = contentResolver, MainImageWidth) {}

        LazyVerticalGrid(columns = GridCells.Adaptive(100.dp)) {
            item(key = "new") {
                IconButton(
                    onClick = { picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                    modifier = Modifier.size(ImageWidth.dp, (ImageWidth * ScreenUtils.HPerW).dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "add",
                        Modifier
                            .size(10.dp)
                            .size(20.dp),
                        tint = Color.White
                    )
                }
            }
            item(key = "1") {
                ImagePrev(uriString = bg1, contentResolver, ImageWidth) {
                    bg = bg1
                    prefManager.saveData("t_bg_path", bg)
                }
            }
            item(key = "2") {
                ImagePrev(uriString = bg2, contentResolver, ImageWidth) {
                    bg = bg2
                    prefManager.saveData("t_bg_path", bg)
                }
            }
            item(key = "3") {
                ImagePrev(uriString = bg3, contentResolver, ImageWidth) {
                    bg = bg3
                    prefManager.saveData("t_bg_path", bg)
                }
            }
            item(key = "4") {
                ImagePrev(uriString = bg4, contentResolver, ImageWidth) {
                    bg = bg4
                    prefManager.saveData("t_bg_path", bg)
                }
            }
            item(key = "5") {
                ImagePrev(uriString = bg5, contentResolver, ImageWidth) {
                    bg = bg5
                    prefManager.saveData("t_bg_path", bg)
                }
            }
        }
    }
}

@Composable
fun ImagePrev(uriString: String, contentResolver: ContentResolver, width: Int, onTap: ()->Unit) {
    val uri = Uri.parse(uriString)
    val bitmap = BitmapFromURI(uri = uri, contentResolver = contentResolver)?.asImageBitmap()
    if (bitmap != null) {
        Image(
            bitmap = bitmap,
            contentDescription = "bg-image",
            Modifier
                .size(width.dp, (width * ScreenUtils.HPerW).dp)
                .padding(5.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable { onTap() },
            Alignment.Center,
            ContentScale.FillBounds
        )
    }
}