package com.example.gatherersmap.presentation.location

import android.content.Context
import android.location.LocationManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun HandleGpsAndNetworkStatus() {
    val context = LocalContext.current
    val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    val isNetworkEnabled =
        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    if (!isGpsEnabled) {
        // TODO:  
    }
    if (!isNetworkEnabled) {
        // TODO:
    }
}