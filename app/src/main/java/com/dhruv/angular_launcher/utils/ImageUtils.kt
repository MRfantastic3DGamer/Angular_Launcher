package com.dhruv.angular_launcher.utils

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


@Composable
fun ImageFromUri (uri: Uri, description: String) {
    val context = LocalContext.current
    val bitmap = BitmapFromURI(context.contentResolver, uri)
    if (bitmap != null) {
        Image(bitmap = bitmap.asImageBitmap(), contentDescription = description)
    }
}

fun BitmapFromURI (contentResolver : ContentResolver, uri: Uri): Bitmap? {
    try {
        val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, uri))
        return bitmap
    }catch (e:Exception) {
        println("image load error for uri $uri")
    }
    return null
}

fun encodeToBase64(image: Bitmap): String? {
    val baos = ByteArrayOutputStream()
    image.compress(Bitmap.CompressFormat.PNG, 100, baos)
    val b = baos.toByteArray()
    val imageEncoded: String = Base64.encodeToString(b, Base64.DEFAULT)
    return imageEncoded
}

suspend fun decodeBase64(input: String?): Bitmap? {
    return withContext(Dispatchers.IO){
        try {
            val decodedByte = Base64.decode(input, 0)
            BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.size)
        }catch (e: Exception){
            println("image could not be decoded from string")
            null
        }
    }
}