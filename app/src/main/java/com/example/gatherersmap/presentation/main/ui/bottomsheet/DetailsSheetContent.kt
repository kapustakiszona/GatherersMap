package com.example.gatherersmap.presentation.main.ui.bottomsheet


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.gatherersmap.R
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.presentation.components.DeletingDialogComposable
import com.example.gatherersmap.presentation.components.GradientButtonComponent

@Composable
fun DetailsSheetContent(
    itemSpot: ItemSpot,
    onEditClickListener: (ItemSpot) -> Unit,
    onDeleteClickListener: (ItemSpot) -> Unit
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(itemSpot.image)
            .build(),
        fallback = painterResource(R.drawable.image_placeholder)
    )
    Column(
        modifier = Modifier.padding(start = 20.dp, top = 10.dp, end = 20.dp)
    ) {
        Text(text = "${itemSpot.name}  id:${itemSpot.id}", fontSize = 26.sp)
        Text(
            text = itemSpot.description,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(10.dp))
        Image(
            modifier = Modifier.clip(RoundedCornerShape(10.dp)).size(200.dp),
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        Spacer(Modifier.height(10.dp))
        Buttons(
            onEditClickListener = {
                onEditClickListener(itemSpot)
            },
            onDeleteClickListener = {
                onDeleteClickListener(itemSpot)
            }
        )
    }
}

@Composable
private fun Buttons(
    onEditClickListener: () -> Unit,
    onDeleteClickListener: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
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
            GradientButtonComponent(
                onClick = {
                    onEditClickListener()
                },
                iconVector = Icons.Outlined.Edit,
                text = "Edit",
                gradientColors = listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.primaryContainer
                )
            )
            Spacer(modifier = Modifier.width(10.dp))
            GradientButtonComponent(
                onClick = {
                    showDialog = true
                },
                iconVector = Icons.Outlined.Delete,
                text = "Delete",
                gradientColors = listOf(
                    MaterialTheme.colorScheme.primaryContainer,
                    MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}