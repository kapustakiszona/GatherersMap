package com.example.gatherersmap.presentation.main.ui.bottomsheet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.gatherersmap.R
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.presentation.components.CircularProgressBarComponent
import com.example.gatherersmap.presentation.components.TextFieldComponent
import com.example.gatherersmap.presentation.components.imagePicker.ImagePicker
import com.example.gatherersmap.presentation.components.reusables.SubcomposeRow

@Composable
fun EditDetailsSheetContent(
    itemSpot: ItemSpot,
    onSaveClicked: (ItemSpot) -> Unit,
    onCancelClicked: (ItemSpot) -> Unit,
    insertAndUpdateNetworkProgress: Boolean,
) {
    var modifiedItem by remember { mutableStateOf(itemSpot) }
    var tempImage by rememberSaveable { mutableStateOf(itemSpot.image) }
    var newName by rememberSaveable { mutableStateOf(itemSpot.name) }
    var newDescription by rememberSaveable { mutableStateOf(itemSpot.description) }
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(tempImage)
            .build(),
        fallback = painterResource(R.drawable.image_placeholder),
    )
    val isSaveButtonEnabled by remember { derivedStateOf { modifiedItem != itemSpot } }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
    ) {
        CircularProgressBarComponent(insertAndUpdateNetworkProgress)

        TextFieldComponent(
            currentValue = newName,
            modifiedValue = { newValue ->
                newName = newValue
                modifiedItem = modifiedItem.copy(name = newName)
            },
            label = "Mushroom Name"
        )
        TextFieldComponent(
            currentValue = newDescription,
            modifiedValue = { newValue ->
                newDescription = newValue
                modifiedItem = modifiedItem.copy(description = newDescription)
            },
            label = "Description"
        )
        Row(
            modifier = Modifier
                .padding(8.dp)
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = MaterialTheme.shapes.extraLarge
                )
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImagePicker(
                onImagePick = { uri ->
                    tempImage = uri
                    //saves the image from the preview into an object that will be saved
                    modifiedItem = modifiedItem.copy(image = tempImage)
                }
            )
            Spacer(modifier = Modifier.width(6.dp))
            Image(
                painter = painter,
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.extraLarge),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }
        Buttons(
            onSaveClicked = {
                onSaveClicked(modifiedItem)
            },
            onCancelClicked = {
                onCancelClicked(itemSpot)
            },
            progressState = insertAndUpdateNetworkProgress,
            isOnSaveEnabled = isSaveButtonEnabled
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ButtonPrev() {
    Buttons({}, {}, false, true)
}

@Composable
private fun Buttons(
    onSaveClicked: () -> Unit,
    onCancelClicked: () -> Unit,
    progressState: Boolean,
    isOnSaveEnabled: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubcomposeRow(
            paddingBetween = 20.dp,
            modifier = Modifier
        ) {
            ElevatedButton(
                contentPadding = ButtonDefaults.ContentPadding,
                modifier = Modifier,
                shape = MaterialTheme.shapes.small,
                onClick = { onCancelClicked() },
                enabled = !progressState,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 1.dp
                ),
                border = BorderStroke(
                    width = 0.5.dp,
                    color = MaterialTheme.colorScheme.inversePrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = null,
                    modifier = Modifier
                )
                Text(
                    maxLines = 1,
                    text = "Cancel",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = TextUnit(3f, TextUnitType.Sp),
                )
            }

            Button(
                modifier = Modifier,
                shape = MaterialTheme.shapes.small,
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    disabledContainerColor = MaterialTheme.colorScheme.inverseOnSurface,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                onClick = { onSaveClicked() },
                enabled = !progressState && isOnSaveEnabled,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 2.dp
                ),
                border = if (!progressState && isOnSaveEnabled) BorderStroke(
                    width = 0.5.dp, color = MaterialTheme.colorScheme.inversePrimary
                ) else null
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    modifier = Modifier
                )
                Text(
                    text = "Save",
                    maxLines = 1,
                    fontSize = 26.sp,
                    fontWeight = if (!progressState && isOnSaveEnabled) FontWeight.Normal else FontWeight.ExtraLight,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = TextUnit(3f, TextUnitType.Sp),
                )
            }
        }
        Spacer((Modifier.height(12.dp)))
    }
}