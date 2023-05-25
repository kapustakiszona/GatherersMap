package com.example.gatherersmap

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.FileProvider
import com.example.gatherersmap.presentation.ui.bottomsheet.BottomSheet
import com.example.gatherersmap.presentation.ui.theme.AppTheme
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : ComponentActivity() {


    private var uri: Uri? = null
    private lateinit var currentImagePath: String
    private var pictureUri by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BottomSheet(
                        onCameraInvoke = {
                            invokeCamera()
                        },
                        pictureUri = pictureUri
                    )
                }
            }
        }
    }

    private fun invokeCamera() {
        val file = createImageFile()
        try {
            uri = FileProvider.getUriForFile(this, "com.example.gatherersmap.fileprovider", file)
        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
        }
        getCameraImage.launch(uri)
    }

    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "Gatherers_$timestamp",
            ".jpg",
            imageDirectory
        ).apply {
            currentImagePath = absolutePath
        }
    }

    private val getCameraImage =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                Log.i(TAG, "Image Location: $uri")
                pictureUri = uri.toString()
            } else {
                Log.e(TAG, "Image not saved. $uri")
            }
        }

    companion object {
        const val TAG = "OTAG"
    }

}

