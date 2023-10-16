package com.example.gatherersmap.utils

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import com.example.gatherersmap.MapApp
import java.io.ByteArrayOutputStream

private fun getBitmapFromUri(contentResolver: ContentResolver, fileUri: Uri?): Bitmap? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, fileUri!!))
        } else {
            MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun bitmapToBase64(bitmap: Bitmap?): String? {
    if (bitmap == null) {
        return null
    }
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    val base64 = Base64.encodeToString(byteArray, Base64.DEFAULT)
    return base64.replace("\n", "")
}

fun convertUriToBase64(uri: String?): String {
    val contentResolver = MapApp.instance.contentResolver
    val androidUri = if (uri != null) Uri.parse(uri) else Uri.EMPTY
    val bitmap = getBitmapFromUri(contentResolver, androidUri)
    return bitmapToBase64(bitmap).orEmpty()
}
