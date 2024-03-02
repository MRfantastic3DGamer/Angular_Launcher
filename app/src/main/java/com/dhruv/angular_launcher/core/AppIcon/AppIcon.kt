package com.dhruv.angular_launcher.core.AppIcon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.round
import androidx.core.graphics.drawable.toBitmap
import com.dhruv.angular_launcher.utils.ScreenUtils
import com.example.launcher.Drawing.DrawablePainter

@Composable
fun AppIcon (pkgName: String, style: IconStyle, painter: DrawablePainter?, offset: Offset, selected: Boolean){
    val correctionOffset = ScreenUtils.dpToF(style.size)/2
    Box(
        Modifier
            .size(style.size)
            .offset { (offset - Offset(correctionOffset, correctionOffset)).round() }
            .border(
                style.borderStrokeWidth,
                color = style.borderColor,
                shape = RoundedCornerShape(style.cornerRadios)
            )
            .clip(RoundedCornerShape(style.cornerRadios))
            .background(style.backGroundColor)
    ){
        if (painter != null){
            Image(
                bitmap = painter.drawable.toBitmap(
                    ScreenUtils.dpToF(style.size).toInt(),
                    ScreenUtils.dpToF(style.size).toInt()
                ).asImageBitmap(),
                contentDescription = "icon-${pkgName}",
                Modifier,
                Alignment.Center,
            )
        }
    }
}