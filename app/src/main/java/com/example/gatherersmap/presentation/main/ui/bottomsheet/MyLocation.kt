package com.example.gatherersmap.presentation.main.ui.bottomsheet

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gatherersmap.R
import com.example.gatherersmap.data.datastore.DataStoreRepository
import com.example.gatherersmap.presentation.components.AlertDialogComponent
import com.example.gatherersmap.presentation.components.SnackBarComponent
import com.example.gatherersmap.presentation.main.ui.MainActivity.Companion.TAG
import com.example.gatherersmap.presentation.main.ui.map.PermissionLifecycleRequest
import com.example.gatherersmap.presentation.main.vm.MapViewModel
import com.example.gatherersmap.presentation.main.vm.PermissionResult
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.theapache64.rebugger.Rebugger
import java.util.concurrent.TimeUnit

@SuppressLint("MissingPermission")
@ExperimentalPermissionsApi
@Composable
fun MyLocation(
    snackBarHostState: SnackbarHostState,
    isFabClicked: (Boolean) -> Unit,
    viewModel: MapViewModel = viewModel(),
) {
    val permissionState =
        rememberMultiplePermissionsState(
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    val context = LocalContext.current
    val state by viewModel.permissionResultState.collectAsState()
    var requestState by remember { mutableStateOf(false) }
    val dataStoreManager = DataStoreRepository.getDataStore()
    val hasInitialRequest =
        dataStoreManager.getPermissionRequestStatus()
            .collectAsState(true)
    val allRequiredPermissionsGranted by remember {
        mutableStateOf(permissionState.permissions
            .filter { it.status.isGranted }
            .map { it.permission })
    }

    PermissionLifecycleRequest(permissionState = permissionState)
    FloatingActionButton(
        onClick = {
            viewModel.permissionHandler(permissionState)
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
    when (state) {
        PermissionResult.PERMISSION_GRANTED -> {
//            if (requestState) {
            LocationUpdatesContent(
                usePreciseLocation = allRequiredPermissionsGranted.contains(Manifest.permission.ACCESS_FINE_LOCATION),
                requestStateUpdate = {
                    requestState = it
                },
                request = requestState
            )
//            }
            Log.d(TAG, "PERMISSION_GRANTED requeststate : $requestState")
//            viewModel.locationState = MapLocationClient(context).checkGpsAndNetworkEnabled()
//            viewModel.OnLocationEvent()
        }

        PermissionResult.PERMISSION_RATIONALE -> {
            LaunchedEffect(key1 = true) {
                dataStoreManager.savePermissionRequestStatus(
                    hasInitialRequest = true
                )
            }
            if (requestState)
                AlertDialogComponent(
                    title = "Permission Request",
                    description = "To track your location on the map and use the full functionality, you must give permission",
                    onClick = {
                        permissionState.launchMultiplePermissionRequest()
                        Log.d(TAG, "PERMISSION_RATIONALE  return requeststate false")
                        requestState = false
                    },
                    textButton = "Give Permission"
                )
        }

        PermissionResult.PERMISSION_DENIED -> {
            when {
                requestState && !hasInitialRequest.value -> {
                    LaunchedEffect(key1 = true) {
                        permissionState.launchMultiplePermissionRequest()
                        Log.d(TAG, "PERMISSION_DENIED  return requeststate false")
                        requestState = false
                    }
                }

                requestState && hasInitialRequest.value -> {
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts(
                            "package",
                            context.packageName,
                            null
                        )
                    )
                    SnackBarComponent(
                        snackBarHostState = snackBarHostState,
                        message = "Permission required",
                        textButton = "Go to settings",
                        intent = intent,
                        onSnackBarDismissed = {
                            Log.d(TAG, "SnackBarComponent dismiss return requeststate false")
                            requestState = false
                        },
                        onSnackBarActionPerformed = {
                            Log.d(TAG, "SnackBarComponent OK return requeststate false")
                            requestState = false
                        }
                    )
                }
            }
        }

        PermissionResult.INITIAL -> {}
    }

}

@RequiresPermission(
    anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION],
)
@Composable
fun LocationUpdatesContent(
    usePreciseLocation: Boolean,
    viewModel: MapViewModel = viewModel(),
    requestStateUpdate: (Boolean) -> Unit,
    request: Boolean,
) {
    Log.d(TAG, "LocationUpdatesContent: started")
    val context = LocalContext.current
    val state by viewModel.permissionResultState.collectAsState()
    // The location request that defines the location updates
    var locationRequest by remember {
        mutableStateOf<LocationRequest?>(null)
    }
    Log.d(TAG, "LocationUpdatesContent: locaitionRequest top: $locationRequest")
    var taskOnSuccess by remember { mutableStateOf(false) }
    Log.d(TAG, "LocationUpdatesContent: taskSucces $taskOnSuccess")
    locationRequest = if (state == PermissionResult.PERMISSION_GRANTED) {
        // Define the accuracy based on your needs and granted permissions
        Log.d(TAG, "LocationUpdatesContent: usePreciseLocation $usePreciseLocation")
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
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        if (taskOnSuccess) {
            LocationUpdatesEffect(locationRequest!!) { result ->
                for (currentLocation in result.locations) {
                    viewModel.updateLocations(currentLocation)
                    Log.d(TAG, "LocationUpdatesContent: ${currentLocation.latitude} ")
                }
            }
        }
        task.addOnSuccessListener {
            taskOnSuccess = true
            Log.d(TAG, "LocationUpdatesContent: taskOnSuccess = $taskOnSuccess")
        }

        task.addOnFailureListener { exception ->
            Log.d(TAG, "LocationUpdatesContent: taskOnFailure = $taskOnSuccess")
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(
                        context,
                        1002
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.d(TAG, "LocationUpdatesContent: ${sendEx.message}")
                }
            }
        }
        requestStateUpdate(false)
    }
    Log.d(TAG, "LocationUpdatesContent: state: ${state.name}")
    Log.d(TAG, "LocationUpdatesContent: locationRequest: $locationRequest")
    Rebugger(
        trackMap = mapOf(
            "usePreciseLocation" to usePreciseLocation,
            "viewModel" to viewModel,
            "requestStateUpdate" to requestStateUpdate,
            "request" to request,
            "context" to context,
            "state" to state,
            "locationRequest" to locationRequest,
            "taskOnSuccess" to taskOnSuccess,
        ),
    )
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
    Log.d(TAG, "LocationUpdatesEffect: started")
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