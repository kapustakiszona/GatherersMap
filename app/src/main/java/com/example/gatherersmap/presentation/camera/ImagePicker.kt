package com.example.gatherersmap.presentation.camera

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gatherersmap.R
import com.example.gatherersmap.presentation.ui.components.ElevatedButtonComponent
import com.example.gatherersmap.presentation.vm.MapViewModel

@Composable
fun ImagePicker(
    mapViewModel: MapViewModel = viewModel(),
    onImagePick: (String) -> Unit,
    currentImage: String?
) {
    val tempImage by mapViewModel.temporalPreviewImage.collectAsState()

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
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            hasImage = uri != null
            imageUri = uri
        }
    )
    Column {
        Row {
            ElevatedButtonComponent(
                onClick = {
                    hasImage = false
                    val uri = ComposeFileProvider.getImageUri(context)
                    imageUri = uri
                    cameraLauncher.launch(uri)

                },
                iconVector = ImageVector.vectorResource(R.drawable.add_photo),
                text = "Add photo"
            )
            Spacer(modifier = Modifier.width(8.dp))
            ElevatedButtonComponent(
                onClick = {
                    hasImage = false
                    photoPicker.launch(
                        "image/*"
//                        PickVisualMediaRequest(
//                            ActivityResultContracts.PickVisualMedia.ImageOnly
//                        )
                    )
                },
                iconVector = Icons.Outlined.Add,
                text = "Images"
            )
        }
        if (hasImage && imageUri != null) {
            onImagePick(imageUri.toString())
        }
    }
}