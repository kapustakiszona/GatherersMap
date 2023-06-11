package com.example.gatherersmap.presentation.permissionshandling

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PermissionsViewModel : ViewModel() {
    private var _permissionsState =
        MutableStateFlow<PermissionsDialogState>(PermissionsDialogState.MainDialog)
    val permissionsState = _permissionsState.asStateFlow()

    fun setState(state: PermissionsDialogState) {
        _permissionsState.value = state
    }
}