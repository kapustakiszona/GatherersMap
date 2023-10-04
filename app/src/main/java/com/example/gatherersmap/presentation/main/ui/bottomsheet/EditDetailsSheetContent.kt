package com.example.gatherersmap.presentation.main.ui.bottomsheet

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
import com.example.gatherersmap.presentation.main.ui.MainActivity.Companion.TAG

@Composable
fun EditDetailsSheetContent(
    itemSpot: ItemSpot,
    onSaveClicked: (ItemSpot) -> Unit,
    onCancelClicked: (ItemSpot) -> Unit,
    insertProgress: Boolean,
    editProgress: Boolean,
) {
    var modifiedItem by remember { mutableStateOf(itemSpot) }
    var tempImage by rememberSaveable { mutableStateOf(itemSpot.image) }
    var newName by rememberSaveable { mutableStateOf(itemSpot.name) }
    var newDescription by rememberSaveable { mutableStateOf(itemSpot.description) }
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(tempImage)
            .build(),
        onError = { error ->
            //Log.d(TAG, "error: ${error.result.throwable}")
        },
        fallback = painterResource(R.drawable.image_placeholder),
    )


    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
    ) {
        CircularProgressBarComponent(insertProgress or editProgress)

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
                    shape = RoundedCornerShape(15.dp)
                ).padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImagePicker(
                onImagePick = { uri ->
                    tempImage = uri
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
        Buttons(
            onSaveClicked = {
                modifiedItem = modifiedItem.copy(image = tempImage)
                onSaveClicked(modifiedItem)
                Log.d(TAG, "EditDetailsSheetContent: save clicked")
            },
            onCancelClicked = {
                onCancelClicked(itemSpot)
            },
            progressState = insertProgress or editProgress
        )
    }
}


@Composable
private fun Buttons(
    onSaveClicked: () -> Unit,
    onCancelClicked: () -> Unit,
    progressState: Boolean,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubcomposeRow(
            paddingBetween = 20.dp
        ) {
            ElevatedButton(
                contentPadding = ButtonDefaults.ContentPadding,
                modifier = Modifier,
                onClick = { onCancelClicked() },
                enabled = !progressState,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 2.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                Text(
                    text = "Cancel",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                )
            }
            ElevatedButton(
                modifier = Modifier,
                onClick = { onSaveClicked() },
                enabled = !progressState,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 2.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                Text(
                    text = "Save",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                )
            }
        }
        Spacer((Modifier.height(12.dp)))
    }
}