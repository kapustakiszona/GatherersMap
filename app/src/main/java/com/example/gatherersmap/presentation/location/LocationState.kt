@file:OptIn(ExperimentalPermissionsApi::class)

package com.example.gatherersmap.presentation.location

import com.example.gatherersmap.domain.model.LocationStateModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi

sealed class LocationState {
    object Initial : LocationState()

    data class Failure(val locationModel: LocationStateModel) : LocationState()

    data class Success(val locationModel: LocationStateModel) : LocationState()
}
