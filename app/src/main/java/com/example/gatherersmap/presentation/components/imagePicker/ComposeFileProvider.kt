package com.example.gatherersmap.presentation.components.imagePicker

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.gatherersmap.R
import java.io.File

class ComposeFileProvider : FileProvider(
    R.xml.file_path
) {
    companion object {
        fun getImageUri(context: Context): Uri {
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()

            val file = File.createTempFile(
                "gatherers_image_",
                ".jpg",
                directory
            )
            val authority =
                context.packageName + ".presentation.components.imagePicker.ComposeFileProvider"
            return getUriForFile(context, authority, file)
        }
    }
}