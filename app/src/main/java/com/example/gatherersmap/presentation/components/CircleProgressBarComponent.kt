package com.example.gatherersmap.presentation.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap

@Composable
fun CircularProgressBarComponent(isShowed: Boolean, modifier: Modifier = Modifier) {
    if (isShowed) {
        CircularProgressIndicator(
            modifier = modifier,
            strokeCap = StrokeCap.Round
        )
    }
}
