package com.example.gatherersmap.presentation.main.ui.bottomsheet

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.gatherersmap.R
import com.example.gatherersmap.data.datastore.DataStore
import com.example.gatherersmap.presentation.components.AlertDialogComponent
import com.example.gatherersmap.presentation.permissionshandling.PermissionRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale


@ExperimentalPermissionsApi
@Composable
fun MyLocationFab(
    onFabClickListener: () -> Unit = {},
) {
    var launchRequestPermission by remember { mutableStateOf(false) }
    if (launchRequestPermission) {
        PermissionRequest(
            dataStoreManager = DataStore.getDataStore(),
            isRequestCalled = launchRequestPermission
        )
    }
    FloatingActionButton(
        onClick = {
            launchRequestPermission = true
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
