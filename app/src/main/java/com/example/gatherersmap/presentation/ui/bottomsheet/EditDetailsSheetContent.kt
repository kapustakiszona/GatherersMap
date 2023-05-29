package com.example.gatherersmap.presentation.ui.bottomsheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.gatherersmap.R
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.presentation.camera.ImagePicker
import com.example.gatherersmap.presentation.ui.components.ElevatedButtonComponent
import com.example.gatherersmap.presentation.ui.components.TextFieldComponent
import com.example.gatherersmap.presentation.vm.MapViewModel

@Composable
fun EditDetailsSheetContent(
    itemSpot: ItemSpot,
    onSaveClicked: (ItemSpot) -> Unit,
    onCancelClicked: (ItemSpot) -> Unit,
    mapViewModel: MapViewModel = viewModel()
) {
    val modifiedItem by remember { mutableStateOf(itemSpot) }
    var tempImage by rememberSaveable { mutableStateOf(itemSpot.image) }
    var newName by rememberSaveable { mutableStateOf(itemSpot.name) }
    var newDescription by rememberSaveable { mutableStateOf(itemSpot.description) }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(tempImage)
            .build(),
        fallback = painterResource(R.drawable.image_placeholder),
    )

    Column(
        modifier = Modifier.padding(start = 20.dp, top = 10.dp, end = 20.dp)
    ) {
        Column {
            TextFieldComponent(
                currentValue = newName,
                modifiedValue = { newValue ->
                    newName = newValue
                    modifiedItem.name = newName
                },
                label = "name"
            )
            TextFieldComponent(
                currentValue = newDescription,
                modifiedValue = { newValue ->
                    newDescription = newValue
                    modifiedItem.description = newDescription
                },
                label = "description"
            )
            ImagePicker(
                onImagePick = {
                    tempImage = it
                    modifiedItem.image = tempImage
                },
                currentImage = tempImage
            )
            Image(
                painter = painter,
                modifier = Modifier.size(50.dp),
                contentDescription = null
            )
        }
        Buttons(
            itemSpot = itemSpot,
            modifiedItem = modifiedItem,
            onSaveClicked = onSaveClicked,
            onCancelClicked = onCancelClicked
        )
    }
}


@Composable
private fun Buttons(
    itemSpot: ItemSpot,
    modifiedItem: ItemSpot,
    onSaveClicked: (ItemSpot) -> Unit,
    onCancelClicked: (ItemSpot) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.clip(shape = CircleShape)
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                .padding(start = 4.dp, end = 4.dp)
        ) {
            ElevatedButtonComponent(
                onClick = {
                    onSaveClicked(modifiedItem)
                },
                iconVector = Icons.Outlined.Check,
                text = "Save"
            )
            Spacer(modifier = Modifier.width(4.dp))
            ElevatedButtonComponent(
                onClick = {
                    onCancelClicked(itemSpot)
                },
                iconVector = Icons.Outlined.Close,
                text = "Cancel"
            )
        }
    }
}
