package com.example.gatherersmap.presentation.main.ui.bottomsheet

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.presentation.components.CircularProgressBarComponent
import me.saket.telephoto.zoomable.rememberZoomableState
import me.saket.telephoto.zoomable.zoomable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageViewerComponent(itemSpot: ItemSpot, backClick: () -> Unit) {
    BackHandler {
        backClick()
    }
    val state = rememberZoomableState()

    Box(
        modifier = Modifier
            .background(Color.Black)
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .zoomable(state),
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
            contentScale = ContentScale.Inside
        )
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = { backClick() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color.White
                    )

                }
            },
            title = { Text(text = itemSpot.name, color = Color.White) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )
    }
}