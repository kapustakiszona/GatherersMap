//package com.example.gatherersmap.presentation.main.ui.bottomsheet
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.size
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import coil.compose.SubcomposeAsyncImage
//import coil.request.ImageRequest
//import com.example.gatherersmap.presentation.components.CircularProgressBarComponent
//import net.engawapg.lib.zoomable.rememberZoomState
//import net.engawapg.lib.zoomable.zoomable
//
//
//@Composable
//fun ImageViewComponent(image: String?) {
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = Modifier.background(Color.Black)
//    ) {
//        SubcomposeAsyncImage(
//            modifier = Modifier.zoomable(
//                rememberZoomState()
//            ),
//            model = ImageRequest.Builder(LocalContext.current)
//                .data(image)
//                .build(),
//            contentDescription = null,
//            loading = {
//                Box(
//                    modifier = Modifier.size(40.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressBarComponent(
//                        isShowed = true,
//                    )
//                }
//            },
//            error = {
//                Box(
//                    modifier = Modifier.size(40.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(text = "Oops, something went wrong")
//                }
//            },
//            contentScale = ContentScale.FillBounds
//        )
//    }
//}