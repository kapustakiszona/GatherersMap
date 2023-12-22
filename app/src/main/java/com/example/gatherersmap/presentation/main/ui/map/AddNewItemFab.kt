package com.example.gatherersmap.presentation.main.ui.map

import android.util.Log
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gatherersmap.presentation.components.CircularProgressBarComponent
import com.example.gatherersmap.presentation.components.reusables.ANIMATION_TIME_HALF_SEC
import com.example.gatherersmap.presentation.components.reusables.ANIMATION_TIME_QTR_SEC
import com.example.gatherersmap.presentation.components.reusables.AnimatedScaleInTransition
import com.example.gatherersmap.presentation.main.ui.MainActivity.Companion.TAG

@Composable
fun AddNewItemFab(
    modifier: Modifier = Modifier,
    visibility: Boolean,
    loadingState: Boolean,
    onClick: () -> Unit
) {
    AnimatedScaleInTransition(
        visible = visibility,
        animateDurationExit = ANIMATION_TIME_QTR_SEC,
        animateDurationEnter = ANIMATION_TIME_HALF_SEC
    ) {
        if (loadingState) {
            Log.d(TAG, "PickLocationFab: ProgressBar start")
            CircularProgressBarComponent(true)
        } else {
            ExtendedFloatingActionButton(
                modifier = modifier,
                text = { Text(text = "New mushroom") },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                shape = RoundedCornerShape(15.dp),
                onClick = onClick,
            )
        }
    }
}