package com.example.gatherersmap.presentation.main.ui.snackbar

data class SnackbarNetworkError(
    val textError: String,
    val action: () -> Unit,
)
