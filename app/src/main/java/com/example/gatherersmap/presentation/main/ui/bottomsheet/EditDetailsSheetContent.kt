package com.example.gatherersmap.presentation.main.ui.bottomsheet

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.gatherersmap.presentation.main.ui.MainActivity.Companion.TAG
import com.example.gatherersmap.R
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.presentation.components.imagePicker.ImagePicker
import com.example.gatherersmap.presentation.components.ElevatedButtonComponent
import com.example.gatherersmap.presentation.components.TextFieldComponent

@Composable
fun EditDetailsSheetContent(
    itemSpot: ItemSpot,
    onSaveClicked: (ItemSpot) -> Unit,
    onCancelClicked: (ItemSpot) -> Unit
) {
    val modifiedItem by remember { mutableStateOf(itemSpot) }
    var tempImage by rememberSaveable { mutableStateOf(itemSpot.image) }
    var newName by rememberSaveable { mutableStateOf(itemSpot.name) }
    var newDescription by rememberSaveable { mutableStateOf(itemSpot.description) }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(tempImage)
            .build(),
        onError = { error ->
            Log.d(TAG, "error: ${error.result.throwable}")
        },
        fallback = painterResource(R.drawable.image_placeholder),
    )


    Column(
        modifier = Modifier.padding(start = 20.dp, top = 10.dp, end = 20.dp)
    ) {
        TextFieldComponent(
            currentValue = newName,
            modifiedValue = { newValue ->
                newName = newValue
                modifiedItem.name = newName
            },
            label = "Name"
        )
        TextFieldComponent(
            currentValue = newDescription,
            modifiedValue = { newValue ->
                newDescription = newValue
                modifiedItem.description = newDescription
            },
            label = "Description"
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(15.dp)
                ).padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImagePicker(
                onImagePick = {
                    tempImage = it
                }
            )
            Spacer(modifier = Modifier.width(6.dp))
            Image(
                painter = painter,
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(15.dp)),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Buttons(
            onSaveClicked = {
                modifiedItem.image = tempImage
                onSaveClicked(modifiedItem)
            },
            onCancelClicked = {
                onCancelClicked(itemSpot)
            }
        )
    }
}


@Composable
private fun Buttons(
    onSaveClicked: () -> Unit,
    onCancelClicked: () -> Unit,
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
                    onCancelClicked()
                },
                iconVector = Icons.Outlined.Close,
                text = "Cancel",
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
            Spacer(modifier = Modifier.width(4.dp))
            ElevatedButtonComponent(
                onClick = {
                    onSaveClicked()
                },
                iconVector = Icons.Outlined.Check,
                text = "Save",
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        }
    }
}
