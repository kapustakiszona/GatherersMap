package com.example.gatherersmap.presentation.main.ui.bottomsheet


import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.gatherersmap.R
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.presentation.components.CircularProgressBarComponent
import com.example.gatherersmap.presentation.components.DeletingDialogComposable
import com.example.gatherersmap.presentation.components.reusables.SubcomposeRow

@Composable
@Preview
fun DetailsPreview() {
    DetailsSheetContent(
        itemSpot = ItemSpot(
            name = "Mushroom",
            lat = 1.1,
            description = "Some text about this itemSome text about this itemSome text about this itemSome text about this itemSome text about this itemSome text about this itemSome text about this itemSome text about this itemSome text about this itemSome text about this itemSome text about this itemSome text about this itemSome text about this itemSome text about this itemSome text about this itemSome text about this itemSome text about this itemSome text about this itemSome text about this itemSome text about this itemSome text about this itemSome text about this itemSome text about this itemSome text about this itemSome text about this itemSome text about this itemSome text about this item",
            lng = 1.1,
            image = R.drawable.image_placeholder.toString()
        ),
        onEditClickListener = {},
        onDeleteClickListener = {},
        deleteProgress = false,
        onImageClick = {}
    )
}

@Composable
fun DetailsSheetContent(
    itemSpot: ItemSpot,
    onEditClickListener: (ItemSpot) -> Unit,
    onDeleteClickListener: (ItemSpot) -> Unit,
    deleteProgress: Boolean,
    onImageClick: (ItemSpot) -> Unit
) {
    var isDescriptionExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .padding(start = 20.dp, top = 10.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (deleteProgress) {
            CircularProgressBarComponent(true)
        }
        Text(text = itemSpot.name, fontSize = 26.sp)
        Text(
            modifier = Modifier
                .animateContentSize(animationSpec = tween(100))
                .clickable {
                    isDescriptionExpanded = !isDescriptionExpanded
                },
            text = itemSpot.description,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
            overflow = TextOverflow.Ellipsis,
            maxLines = if (isDescriptionExpanded) Int.MAX_VALUE else 1
        )
//        Text(text = "  id:${itemSpot.id}", fontSize = 8.sp)
        Spacer(Modifier.height(10.dp))
        SubcomposeAsyncImage(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .aspectRatio(3f / 2f)
                .clickable {
                    onImageClick(itemSpot)
                },
            model = ImageRequest.Builder(LocalContext.current)
                .data(itemSpot.image)
                .diskCacheKey(itemSpot.image)
                .diskCachePolicy(CachePolicy.ENABLED)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
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
            }
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
            var showDialog by rememberSaveable { mutableStateOf(false) }
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
                shape = MaterialTheme.shapes.small,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 2.dp
                ),
                border = BorderStroke(
                    width = 0.5.dp,
                    color = MaterialTheme.colorScheme.inversePrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = null,
                    modifier = Modifier
                )
                Text(
                    text = "Edit",
                    maxLines = 1,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = TextUnit(3f, TextUnitType.Sp),
                )
            }

            ElevatedButton(
                modifier = Modifier,
                onClick = { showDialog = true },
                enabled = !loadingInProgress,
                shape = MaterialTheme.shapes.small,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 2.dp
                ),
                border = BorderStroke(
                    width = 0.5.dp,
                    color = MaterialTheme.colorScheme.inversePrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = null,
                    modifier = Modifier
                )
                Text(
                    text = "Delete",
                    maxLines = 1,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.SansSerif,
                    letterSpacing = TextUnit(3f, TextUnitType.Sp),
                )
            }
        }
        Spacer((Modifier.height(12.dp)))
    }
}