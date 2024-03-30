package com.dhruv.angular_launcher.settings_screen.presentation.components.shader

import android.content.res.Resources
import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.dhruv.angular_launcher.accessible_screen.components.glsl_art.AllDefaultTextures
import com.dhruv.angular_launcher.settings_screen.presentation.components.H3

fun LazyListScope.textureViewer(
    visibility: Boolean,
    allMaps: Map<String, List<Pair<Int, IntRange>>>,
    selectedTextures: Set<Pair<Int,Int>>,
    resources: Resources,
    onSelect: (Int, Int) -> Unit
) {
//    val expand by remember { mutableStateOf(false) }

    allMaps.keys.forEach { group ->
        item {
            AnimatedVisibility(visible = visibility) {
                var open by remember { mutableStateOf(false) }
                Column {
                    Row (
                        Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .height(34.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.Black)
                            .clickable
                            {
                                open = !open
                                println(allMaps[group])
                            },
                        Arrangement.SpaceBetween,
                        Alignment.CenterVertically
                    ) {
                        Row {
                            Spacer(modifier = Modifier.width(10.dp))
                            H3(text = group)
                        }
                        Row {
                            Icon(
                                imageVector = if (open) Icons.Rounded.KeyboardArrowDown else Icons.Rounded.KeyboardArrowUp,
                                contentDescription = "open-close",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    }

                    AnimatedVisibility(visible = open) {
                        LazyHorizontalGrid(
                            rows = GridCells.Adaptive(100.dp),
                            modifier = Modifier
                                .height(400.dp)
                        ) {
                            allMaps[group]!!.forEach { pack ->
                                val image = pack.first
                                val imageBitmap = BitmapFactory.decodeResource(resources, image)
                                this.items(pack.second.toList()){ stream ->
                                    ImageStreamView(
                                        modifier = Modifier
                                            .clickable { onSelect(image, stream) },
                                        key = pack.first.toString(),
                                        bitmap = imageBitmap.asImageBitmap(),
                                        stream = stream,
                                        selected = selectedTextures.contains(Pair(image, stream))
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}

fun getAllAvailableDefaultMaps(): Map<String, List<Pair<Int, IntRange>>> {
    val textureKeys = AllDefaultTextures.keys.toList()
    val map = mutableMapOf<String, MutableList<Pair<Int, IntRange>>>()
    textureKeys.forEach{
        val parts = it.split('_')
        var name = ""
        for (i in 0 until parts.size-2)
            name += parts[i]
        val packedValues = Pair(AllDefaultTextures[it]!!, 0 until  (parts[parts.size - 1].toInt()) - (parts[parts.size - 2].toInt()) + 1)
        if (map.containsKey(name)) map[name]!!.add(packedValues)
        else map[name] = mutableListOf(packedValues)
    }
    return map
}

val ColorFiltersForRGBA = listOf(
    ColorFilter.colorMatrix(
        ColorMatrix(
            floatArrayOf(
                1f, 0f, 0f, 0f, 0f,
                1f, 0f, 0f, 0f, 0f,
                1f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f, 255f,
            )
        )
    ),
    ColorFilter.colorMatrix(
        ColorMatrix(
            floatArrayOf(
                0f, 1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f, 255f,
            )
        )
    ),
    ColorFilter.colorMatrix(
        ColorMatrix(
            floatArrayOf(
                0f, 0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f, 0f,
                0f, 0f, 0f, 0f, 255f,
            )
        )
    ),
    ColorFilter.colorMatrix(
        ColorMatrix(
            floatArrayOf(
                0f, 0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f, 0f,
                0f, 0f, 0f, 0f, 255f,
            )
        )
    ),
)

val RGBA = "RGBA"
@Composable
fun ImageStreamView(
    modifier: Modifier = Modifier,
    key:String,
    bitmap: ImageBitmap,
    stream: Int,
    selected: Boolean
) {
    Box {
        Image(
            bitmap = bitmap,
            contentDescription = "$key-R",
            modifier.size(100.dp, 100.dp),
            colorFilter = ColorFiltersForRGBA[stream],
            alignment = Alignment.Center,
            contentScale = ContentScale.FillBounds
        )
        if (selected){
            Icon(
                imageVector = Icons.Rounded.Done,
                contentDescription = "done",
                Modifier.background(Color.Black),
                tint = Color.White,
            )
        }
    }
}