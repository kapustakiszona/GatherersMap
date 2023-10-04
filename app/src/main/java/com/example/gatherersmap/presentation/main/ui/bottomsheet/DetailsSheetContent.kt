package com.example.gatherersmap.presentation.main.ui.bottomsheet


import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.presentation.components.CircularProgressBarComponent
import com.example.gatherersmap.presentation.components.DeletingDialogComposable
import com.example.gatherersmap.presentation.components.reusables.SubcomposeRow
import com.example.gatherersmap.presentation.main.ui.MainActivity.Companion.TAG

@Composable
fun DetailsSheetContent(
    itemSpot: ItemSpot,
    onEditClickListener: (ItemSpot) -> Unit,
    onDeleteClickListener: (ItemSpot) -> Unit,
    deleteProgress: Boolean,
) {
    Column(
        modifier = Modifier.padding(start = 20.dp, top = 10.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (deleteProgress) {
            CircularProgressBarComponent(true)
        }
        Text(text = "${itemSpot.name}  id:${itemSpot.id}", fontSize = 26.sp)
        Text(
            text = itemSpot.description,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(10.dp))
        Log.d(TAG, "DetailsSheetContent: details image ->> ${itemSpot.image}")
        SubcomposeAsyncImage(
            modifier = Modifier.clip(RoundedCornerShape(10.dp)).size(200.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(itemSpot.image)
                .build(),
            contentDescription = null,
            loading = {
                Box(
                    modifier = Modifier.size(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressBarComponent(
                        isShowed = true,
                    )
                }
            },
            error = {
                Box(
                    modifier = Modifier.size(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Oops, something went wrong")
                }
            },
            contentScale = ContentScale.FillBounds
        )
        Spacer(Modifier.height(10.dp))
        Buttons(
            onEditClickListener = {
                onEditClickListener(itemSpot)
            },
            onDeleteClickListener = {
                onDeleteClickListener(itemSpot)
            },
            loadingInProgress = deleteProgress
        )
    }
}

@Composable
private fun Buttons(
    onEditClickListener: () -> Unit,
    onDeleteClickListener: () -> Unit,
    loadingInProgress: Boolean,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubcomposeRow(
            paddingBetween = 20.dp
        ) {
            var showDialog by remember { mutableStateOf(false) }
            if (showDialog) {
                DeletingDialogComposable(
                    onDeleteItem = {
                        onDeleteClickListener()
                    },
                    onClose = { showDialog = false }
                )
            }
            ElevatedButton(
                contentPadding = ButtonDefaults.ContentPadding,
                modifier = Modifier,
                onClick = { onEditClickListener() },
                enabled = !loadingInProgress,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 2.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                Text(
                    text = "Edit",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                )
            }
            ElevatedButton(
                modifier = Modifier,
                onClick = { showDialog = true },
                enabled = !loadingInProgress,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 2.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                Text(
                    text = "Delete",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                )
            }
        }
        Spacer((Modifier.height(12.dp)))
    }
}