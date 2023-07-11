@file:OptIn(ExperimentalPermissionsApi::class, ExperimentalPermissionsApi::class)

package com.example.gatherersmap.presentation.location

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import com.example.gatherersmap.domain.model.LocationStateModel
import com.example.gatherersmap.presentation.main.ui.MainActivity.Companion.TAG
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class MapLocationClient(
    private val context: Context,
) {
    private var client: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private lateinit var task: Task<LocationSettingsResponse>

    @SuppressLint("MissingPermission")
    fun getLocationUpdates(interval: Long): Flow<Location> {
        Log.d(TAG, "getLocationUpdates: Running")
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

    fun checkGpsAndNetworkEnabled(): LocationStateModel {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled =
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//        task.addOnFailureListener {
//            if (it is ResolvableApiException) {
//                try {
//                    it.startResolutionForResult(context as Activity, 1002)
//                } catch (sendEx: IntentSender.SendIntentException) {
//                }
//            }
//        }
        return LocationStateModel(gpsStatus = isGpsEnabled, networkStatus = isNetworkEnabled)

        // throw LocationClient.LocationException("GPS is disabled")
    }
}