package com.example.gatherersmap.presentation.main.ui.bottomsheet

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.gatherersmap.R


@Composable
fun DefiningPositionFab(
    onFabClickListener: () -> Unit
) {
    FloatingActionButton(
        onClick = {
            onFabClickListener()
        },
        shape = CircleShape,
        content = {
            Icon(
                painter = painterResource(id = R.drawable.gps_off),
                contentDescription = null,
                modifier = Modifier.size(44.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    )
}