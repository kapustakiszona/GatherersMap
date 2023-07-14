package com.example.gatherersmap.presentation.location

import android.Manifest
import android.app.Activity
import android.content.IntentSender
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gatherersmap.presentation.main.ui.MainActivity
import com.example.gatherersmap.presentation.main.vm.MapViewModel
import com.example.gatherersmap.presentation.main.vm.PermissionResult
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import java.util.concurrent.TimeUnit

@RequiresPermission(
    anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION],
)
@Composable
fun LocationUpdates(
    usePreciseLocation: Boolean,
    viewModel: MapViewModel = viewModel(),
    requestStateUpdate: (Boolean) -> Unit,
    request: Boolean,
) {
    Log.d(MainActivity.TAG, "LocationUpdatesContent: started")
    val context = LocalContext.current
    val state by viewModel.permissionResultState.collectAsState()
    var locationRequest by remember {
        mutableStateOf<LocationRequest?>(null)
    }
    Log.d(MainActivity.TAG, "LocationUpdatesContent: locationRequest top: $locationRequest")

    locationRequest = if (state == PermissionResult.PERMISSION_GRANTED) {
        // Define the accuracy based on your needs and granted permissions
        Log.d(MainActivity.TAG, "LocationUpdatesContent: usePreciseLocation $usePreciseLocation")
        val priority = if (usePreciseLocation) {
            Priority.PRIORITY_HIGH_ACCURACY
        } else {
            Priority.PRIORITY_BALANCED_POWER_ACCURACY
        }
        LocationRequest.Builder(
            priority,
            TimeUnit.SECONDS.toMillis(3)
        ).build()
    } else {
        null
    }
    // Only register the location updates effect when we have a request
    if (locationRequest != null) {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest!!)
        val client: SettingsClient = LocationServices.getSettingsClient(context as Activity)
        // val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        var taskOnSuccess by remember { mutableStateOf(client.checkLocationSettings(builder.build())) }
        Log.d(MainActivity.TAG, "LocationUpdatesContent: taskSuccess $taskOnSuccess")
        if (taskOnSuccess.isSuccessful) {
            LocationUpdatesEffect(locationRequest!!) { result ->
                for (currentLocation in result.locations) {
                    viewModel.updateLocations(currentLocation)
                    Log.d(MainActivity.TAG, "LocationUpdatesContent: ${currentLocation.latitude} ")
                }
            }
        }
        LaunchedEffect(key1 = taskOnSuccess) {
            taskOnSuccess.addOnSuccessListener {
                //taskOnSuccess = task.isSuccessful
                Log.d(MainActivity.TAG, "LocationUpdatesContent: taskOnSuccess = $taskOnSuccess")
            }

            taskOnSuccess.addOnFailureListener { exception ->
                Log.d(MainActivity.TAG, "LocationUpdatesContent: taskOnFailure = $taskOnSuccess")
                if (exception is ResolvableApiException) {
                    try {
                        exception.startResolutionForResult(
                            context,
                            1002
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        Log.d(MainActivity.TAG, "LocationUpdatesContent: ${sendEx.message}")
                    }
                }
            }
        }
    }
    Log.d(MainActivity.TAG, "LocationUpdatesContent: state: ${state.name}")
    Log.d(MainActivity.TAG, "LocationUpdatesContent: locationRequest: $locationRequest")
}

@RequiresPermission(
    anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION],
)
@Composable
fun LocationUpdatesEffect(
    locationRequest: LocationRequest,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onUpdate: (result: LocationResult) -> Unit,
) {
    Log.d(MainActivity.TAG, "LocationUpdatesEffect: started")
    val context = LocalContext.current
    val currentOnUpdate by rememberUpdatedState(newValue = onUpdate)

    // Whenever on of these parameters changes, dispose and restart the effect.
    DisposableEffect(locationRequest, lifecycleOwner) {
        val locationClient = LocationServices.getFusedLocationProviderClient(context)
        val locationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                currentOnUpdate(result)
            }
        }
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                locationClient.requestLocationUpdates(
                    locationRequest, locationCallback, Looper.getMainLooper(),
                )
            } else if (event == Lifecycle.Event.ON_STOP) {
                locationClient.removeLocationUpdates(locationCallback)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            locationClient.removeLocationUpdates(locationCallback)
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}