package com.example.gatherersmap.presentation.main.ui.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SnackBarNetworkErrorManager(
    networkErrorFlow: SharedFlow<SnackbarNetworkError>,
    snackbarHostState: SnackbarHostState,
) {
    LaunchedEffect(key1 = networkErrorFlow) {
        networkErrorFlow.collectLatest { errorContent ->
            val snackbarResult = snackbarHostState.showSnackbar(
                message = errorContent.textError,
                duration = SnackbarDuration.Long,
                actionLabel = "Retry"
            )
            when (snackbarResult) {
                SnackbarResult.Dismissed -> {}
                SnackbarResult.ActionPerformed -> {
                    errorContent.action.invoke()
                }
            }
        }
    }
}