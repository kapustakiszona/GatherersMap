package com.example.gatherersmap.presentation.components

import android.content.Intent
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun SnackBarComponent(
    snackBarHostState: SnackbarHostState,
    message: String,
    textButton: String,
    intent: Intent,
    onSnackBarDismissed: () -> Unit = {},
    onSnackBarActionPerformed: () -> Unit = {},
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        val snackBarResult = snackBarHostState.showSnackbar(
            message = message,
            actionLabel = textButton,
            duration = SnackbarDuration.Indefinite
        )
        when (snackBarResult) {
            SnackbarResult.Dismissed -> {
                onSnackBarDismissed()
            }

            SnackbarResult.ActionPerformed -> {
                onSnackBarActionPerformed()
                context.startActivity(intent)
            }
        }
    }

}