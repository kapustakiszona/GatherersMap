package com.example.gatherersmap.presentation.ui.bottomsheet

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.gatherersmap.R
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.presentation.ui.components.ElevatedButtonComponent

@Composable
fun EditDetailsSheetContent(
    itemSpot: ItemSpot,
    onSaveClicked: (ItemSpot) -> Unit,
    onCancelClicked: (ItemSpot) -> Unit,
    onAddImageClicked: () -> Unit,
    pictureUri: String
) {
    val modifiedItem by remember { mutableStateOf(itemSpot) }
    var newName by rememberSaveable { mutableStateOf(itemSpot.name) }
    var newDescription by rememberSaveable { mutableStateOf(itemSpot.description) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.padding(start = 20.dp, top = 10.dp, end = 20.dp)
    ) {
        Column {
            OutlinedTextField(
                value = newName,
                onValueChange = { text ->
                    newName = text
                    modifiedItem.name = newName
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                label = {
                    Text(text = "Name")
                },
                shape = RoundedCornerShape(15.dp)
            )
            OutlinedTextField(
                value = newDescription,
                onValueChange = { description ->
                    newDescription = description
                    modifiedItem.description = newDescription
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                label = {
                    Text(text = "Description")
                },
                shape = RoundedCornerShape(15.dp)
            )
            ElevatedButtonComponent(
                onClick = { onAddImageClicked() },
                iconVector = ImageVector.vectorResource(R.drawable.add_photo),
                text = "Add image"
            )
            AsyncImage(
                modifier = Modifier.size(50.dp).clip(RoundedCornerShape(20.dp)),
                model = pictureUri,
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }
        Buttons(
            itemSpot = itemSpot,
            modifiedItem = modifiedItem,
            onSaveClicked = onSaveClicked,
            onCancelClicked = onCancelClicked,
            pictureUri = pictureUri
        )
    }
}

@Composable
private fun Buttons(
    itemSpot: ItemSpot,
    modifiedItem: ItemSpot,
    onSaveClicked: (ItemSpot) -> Unit,
    onCancelClicked: (ItemSpot) -> Unit,
    pictureUri: String
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
                    if (pictureUri.isNotEmpty()) {
                        modifiedItem.image = pictureUri
                    }
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

fun getResourceUri(context: Context, resId: Int): String {
    return Uri.parse(
        ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" +
                context.resources.getResourcePackageName(resId) +
                '/' +
                context.resources.getResourceTypeName(resId) +
                '/' +
                context.resources.getResourceEntryName(resId)
    ).toString()
}