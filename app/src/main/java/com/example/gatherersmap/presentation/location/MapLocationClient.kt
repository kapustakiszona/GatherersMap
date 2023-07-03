package com.example.gatherersmap.presentation.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class MapLocationClient(
    private val context: Context,
) {
    private var client: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun getLocationUpdates(interval: Long): Flow<Location> {
        return callbackFlow {
            val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, interval).apply {
                setIntervalMillis(interval)
            }.build()
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    result.locations.lastOrNull()?.let { location ->
                        launch { send(location) }
                    }
                }
            }
            client.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )
            awaitClose {
                client.removeLocationUpdates(locationCallback)
            }
        }
    }

    fun checkGpsAndNetworkEnabled() {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled =
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (!isGpsEnabled && !isNetworkEnabled) {
            throw LocationClient.LocationException("GPS is disabled")
        }
    }
}