package com.example.gatherersmap.domain.model

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus

@ExperimentalPermissionsApi
data class LocationStateModel(
    // var permissionStatus: PermissionStatus = PermissionStatus.Denied(shouldShowRationale = false),
    var gpsStatus: Boolean = false,
    var networkStatus: Boolean = false,
)
