package com.example.gatherersmap.presentation.ui.bottomsheet


import androidx.compose.foundation.background
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.gatherersmap.R
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.presentation.ui.components.DeletingDialogComposable
import com.example.gatherersmap.presentation.ui.components.ElevatedButtonComponent

@Composable
fun DetailsSheetContent(
    itemSpot: ItemSpot,
    onEditClickListener: (ItemSpot) -> Unit,
    onDeleteClickListener: (ItemSpot) -> Unit
) {
    Column(
        modifier = Modifier.padding(start = 20.dp, top = 10.dp, end = 20.dp)
    ) {
        Text(text = "${itemSpot.name}  id:${itemSpot.id}", fontSize = 26.sp)
        Text(
            text = itemSpot.description,
            fontSize = 16.sp,
            color = MaterialTheme.colors.onBackground
        )
        Spacer(Modifier.height(10.dp))
        AsyncImage(
            modifier = Modifier.clip(RoundedCornerShape(10.dp)).size(200.dp),
            model = if (itemSpot.image.isNullOrEmpty()) R.drawable.image_placeholder else itemSpot.image,
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        Spacer(Modifier.height(10.dp))
        Buttons(
            itemSpot = itemSpot,
            onEditClickListener = onEditClickListener,
            onDeleteClickListener = onDeleteClickListener
        )
    }
}

@Composable
private fun Buttons(
    itemSpot: ItemSpot,
    onEditClickListener: (ItemSpot) -> Unit,
    onDeleteClickListener: (ItemSpot) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.clip(shape = CircleShape)
                .background(color = androidx.compose.material3.MaterialTheme.colorScheme.secondaryContainer)
                .padding(start = 4.dp, end = 4.dp)
        ) {
            var showDialog by remember { mutableStateOf(false) }
            if (showDialog) {
                DeletingDialogComposable(
                    onDeleteItem = {
                        onDeleteClickListener(itemSpot)
                    },
                    onClose = { showDialog = false }
                )
            }
            ElevatedButtonComponent(
                onClick = {
                    onEditClickListener(itemSpot)
                },
                iconVector = Icons.Outlined.Edit,
                text = "Edit"
            )
            Spacer(modifier = Modifier.width(10.dp))
            ElevatedButtonComponent(
                onClick = {
                    showDialog = true
                },
                iconVector = Icons.Outlined.Delete,
                text = "Delete"
            )
        }
    }
}