package com.dhruv.angular_launcher.composable_components.glass_slab

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.Dp
import com.dhruv.angular_launcher.utils.PathToShape
import com.dhruv.angular_launcher.utils.ScreenUtils

@Composable
fun GlassSlab (
    modifier: Modifier,
    path: Path,
    blur: Dp,
    image: ImageBitmap,
    alpha: Float = 1f,
    shineBrush: Brush = Brush.linearGradient(
        0f to Color.Transparent,
        0.5f to Color.White,
        1f to Color.Transparent,
        start = Offset(0f,ScreenUtils.screenHeight),
        end = Offset(ScreenUtils.screenWidth, 0f),
        tileMode = TileMode.Repeated
    ),
){
    GlassSlab(modifier = modifier, path = path, blurX = blur, blurY = blur, image = image, alpha = alpha, shineBrush = shineBrush)
}

@Composable
fun GlassSlab (
    modifier: Modifier,
    path: Path,
    blurX: Dp,
    blurY: Dp,
    image: ImageBitmap,
    alpha: Float = 1f,
    shineBrush: Brush = Brush.linearGradient(
        0f to Color.Transparent,
        0.5f to Color.White,
        1f to Color.Transparent,
        start = Offset(0f,ScreenUtils.screenHeight),
        end = Offset(ScreenUtils.screenWidth, 0f),
        tileMode = TileMode.Repeated
    ),
){
    Box(
        modifier = modifier
            .drawWithCache {
                onDrawBehind {
                    val colorMatrix = ColorMatrix(
                        floatArrayOf(
                            1f, 0f, 0f, 0f, 0f,
                            0f, 1f, 0f, 0f, 0f,
                            0f, 0f, 1f, 0f, 0f,
                            0f, 0f, 0f, 0.5f, 0f // Adjust the last value (0.5f) for blur intensity
                        )
                    )

                    this.drawImage(
                        image = image,
                        topLeft = Offset.Zero,
                        style = Fill,
//                        colorFilter = ColorFilter.colorMatrix( colorMatrix ),
                        alpha = alpha,
                    )
                    this.drawRect(
                        shineBrush,
                        topLeft = Offset.Zero,
                        size = Size(ScreenUtils.screenHeight, ScreenUtils.screenHeight),
                        alpha = alpha,
                        style = Fill,
                    )
                }
            }
            .blur(blurX, blurY)
            .clip(PathToShape(path)),
    )
}