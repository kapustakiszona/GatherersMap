package com.example.gatherersmap.presentation.components.reusables

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun SnackBarComponent(
    snackBarHostState: SnackbarHostState,
    message: String? = null,
    textButton: String,
    duration: SnackbarDuration,
    onSnackBarDismissed: () -> Unit = {},
    onSnackBarActionPerformed: () -> Unit = {},
) {
    LaunchedEffect(key1 = null) {
        message ?: return@LaunchedEffect
        val snackBarResult = snackBarHostState.showSnackbar(
            message = message,
            actionLabel = textButton,
            duration = duration
        )
        when (snackBarResult) {
            SnackbarResult.Dismissed -> {
                onSnackBarDismissed()
            }

            SnackbarResult.ActionPerformed -> {
                onSnackBarActionPerformed()
            }
        }
    }

}