package com.example.gatherersmap.presentation.main.ui.bottomsheet

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.gatherersmap.R
import com.example.gatherersmap.data.datastore.DataStoreRepository
import com.example.gatherersmap.presentation.permissionshandling.PermissionRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@ExperimentalPermissionsApi
@Composable
fun MyLocation(
    snackBarHostState: SnackbarHostState,
    isFabClicked: (Boolean) -> Unit,
) {
    var requestState by remember { mutableStateOf(false) }
    val dataStoreManager = DataStoreRepository.getDataStore()
    val hasInitialRequest =
        dataStoreManager.getPermissionRequestStatus()
            .collectAsState(true)
    if (requestState) {
        PermissionRequest(
            hasInitialRequest = hasInitialRequest.value,
            dataStoreManager = dataStoreManager,
            snackBarHostState = snackBarHostState,
            resultCallback = {
                requestState = it
            }
        )
    }
    FloatingActionButton(
        onClick = {
            isFabClicked(true)
            requestState = true
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
