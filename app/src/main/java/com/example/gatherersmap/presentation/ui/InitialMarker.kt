package com.example.gatherersmap.presentation.ui

import com.google.android.gms.maps.model.LatLng

data class InitialMarker(
    var isVisible: Boolean = false,
    var latLng: LatLng? = null
)
