package com.example.gatherersmap.presentation.components.reusables

import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun SnackBarComponent(
    snackBarHostState: SnackbarHostState,
    message: String,
    textButton: String,
    duration: SnackbarDuration,
    onSnackBarDismissed: () -> Unit = {},
    onSnackBarActionPerformed: () -> Unit = {},
) {
    LaunchedEffect(key1 = true) {
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