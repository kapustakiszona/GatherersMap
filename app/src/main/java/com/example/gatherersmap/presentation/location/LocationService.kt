package com.example.gatherersmap.presentation.location

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.location.Location
import android.util.Log
import com.example.gatherersmap.presentation.main.ui.MainActivity.Companion.TAG
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.Priority


@SuppressLint("MissingPermission")
fun locationService(context: Context, currentLocationObserver: (Location) -> Unit) {
    val locationRequest =
        LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 10000)
            .build()
    val builder =
        LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    builder.setAlwaysShow(true)
    val task = LocationServices.getSettingsClient(context)
        .checkLocationSettings(builder.build())
    task.addOnCompleteListener {
        try {
            it.getResult(ApiException::class.java)
            // Location service enabled
        } catch (exception: ApiException) {
            when (exception.statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    try {
                        val resolvable: ResolvableApiException =
                            exception as ResolvableApiException
                        resolvable.startResolutionForResult(
                            context as Activity,
                            1002
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        Log.d(TAG, "Pending Intent unable to execute request.")
                    }
                }

                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    Log.d(
                        TAG,
                        "Location settings are inadequate, and cannot be fixed here. Dialog not created."
                    )
                }
            }
        }
    }
    val locationClient = LocationServices.getFusedLocationProviderClient(context)
    locationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            currentLocationObserver(location)
        }
    }
}