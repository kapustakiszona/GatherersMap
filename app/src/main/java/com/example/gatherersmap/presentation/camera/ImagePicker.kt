package com.example.gatherersmap.presentation.camera

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import com.example.gatherersmap.R
import com.example.gatherersmap.presentation.ui.components.ElevatedButtonComponent

@Composable
fun ImagePicker(
    onImagePick: (String) -> Unit
) {

    val context = LocalContext.current

    var hasImage by remember { mutableStateOf(false) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            hasImage = success
        }
    )

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            hasImage = uri != null
            imageUri = uri
        }
    )


    Column {
        ElevatedButtonComponent(
            onClick = {
                hasImage = false
                val uri = ComposeFileProvider.getImageUri(context)
                imageUri = uri
                cameraLauncher.launch(uri)

            },
            iconVector = ImageVector.vectorResource(R.drawable.add_photo),
            text = "Add photo",
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
        ElevatedButtonComponent(
            onClick = {
                hasImage = false
                photoPicker.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            },
            iconVector = Icons.Outlined.Add,
            text = "Images",
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
        if (hasImage && imageUri != null) {
            val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
            context.contentResolver.takePersistableUriPermission(imageUri!!, flag)
            onImagePick(imageUri.toString())
        }
    }
}