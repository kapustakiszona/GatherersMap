package com.example.gatherersmap.presentation.main.vm

import android.location.Location
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gatherersmap.data.ItemSpotRepositoryImpl
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.domain.model.LocationStateModel
import com.example.gatherersmap.navigation.BottomSheetScreenState
import com.example.gatherersmap.presentation.location.LocationState
import com.example.gatherersmap.presentation.main.ui.MainActivity.Companion.TAG
import com.example.gatherersmap.presentation.main.ui.map.MapEvent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
class MapViewModel(
    private val repository: ItemSpotRepositoryImpl,
) : ViewModel() {

    private val _sheetState =
        MutableStateFlow<BottomSheetScreenState>(BottomSheetScreenState.Initial)
    val sheetState = _sheetState.asStateFlow()

    private val _itemsState = MutableStateFlow(MapState())
    val itemsState = _itemsState.asStateFlow()

    private val _temporalMarker = MutableStateFlow<LatLng?>(null)
    val temporalMarker = _temporalMarker.asStateFlow()

    private val _permissionResultState =
        MutableStateFlow(PermissionResult.INITIAL)
    val permissionResultState = _permissionResultState.asStateFlow()

    //var locationState by mutableStateOf(LocationStateModel())

    private val _locationUpdates = MutableStateFlow<Location?>(null)
    val locationUpdates = _locationUpdates.asStateFlow()


    // TODO: попробовать позвращать в функции созданные енамы, через которые в композабле вызывать нужные диалоги
// TODO: rememberPermission не работает 

    fun updateLocations(location: Location) {
        _locationUpdates.value = location
    }

    init {
        viewModelScope.launch {
            repository.getItemSpots().collect {
                _itemsState.value = _itemsState.value.copy(itemSpots = it)
            }
        }
    }

    fun permissionHandler(permissionState: MultiplePermissionsState) {
        Log.d(TAG, "permissionState started / state: ${permissionState.revokedPermissions}")
        _permissionResultState.update {
            if (permissionState.allPermissionsGranted) {
                PermissionResult.PERMISSION_GRANTED
            } else {
                if (!permissionState.shouldShowRationale) {
                    PermissionResult.PERMISSION_DENIED
                } else {
                    PermissionResult.PERMISSION_RATIONALE
                }
            }
        }
    }

    /**
     *  Возможно эта функция и сам стейт не нужны и можно обращаться к Модели
     *  и через иф элс вызывать диалоги
     */
//    @Composable
//    fun OnLocationEvent() {
//        when {
//            !locationState.gpsStatus -> {
//                Log.e(TAG, "GPS is disabled")
//            }
//
//            !locationState.networkStatus -> {
//                Log.e(TAG, "NETWORK is disabled")
//            }
//
//            locationState.gpsStatus and locationState.networkStatus -> {
//                Log.d(TAG, "Location can be showed")
//            }
//        }
//    }

    fun onEvent(event: MapEvent) {
        when (event) {

            is MapEvent.Initial -> {
                removeTemporalMarker()
                setDefaultSheetState()
            }

            is MapEvent.OnSaveItemClick -> {
                insertItemSpot(
                    ItemSpot(
                        lat = event.spot.lat,
                        lng = event.spot.lng,
                    )
                )
            }

            is MapEvent.OnAddItemLongClick -> {
                _temporalMarker.update {
                    event.latLng
                }
                _sheetState.update {
                    BottomSheetScreenState.Add(
                        itemSpot = ItemSpot(
                            lng = event.latLng.longitude,
                            lat = event.latLng.latitude
                        )
                    )
                }
            }

            is MapEvent.OnDeleteItemClick -> {
                deleteItemSpot(event.spot)
            }

            is MapEvent.OnDetailsItemClick -> {
                removeTemporalMarker()
                _sheetState.update {
                    BottomSheetScreenState.Details(
                        itemSpot = event.spot
                    )
                }
            }

            is MapEvent.OnEditItemClick -> {
                _sheetState.update {
                    BottomSheetScreenState.Edit(
                        itemSpot = event.spot,
                    )
                }
            }
        }
    }

    fun insertItemSpot(itemSpot: ItemSpot) {
        viewModelScope.launch {
            repository.insertItemSpot(itemSpot)
        }
    }

    fun deleteItemSpot(itemSpot: ItemSpot) {
        viewModelScope.launch {
            repository.deleteItemSpot(itemSpot)
        }
    }

    fun updateItemSpot(itemSpot: ItemSpot) {
        viewModelScope.launch {
            repository.insertItemSpot(itemSpot)
        }
    }

    private fun setDefaultSheetState() {
        viewModelScope.launch {
            delay(150)
            _sheetState.update {
                BottomSheetScreenState.Initial
            }
        }
    }

    private fun removeTemporalMarker() {
        _temporalMarker.update {
            null
        }
    }
}

enum class PermissionResult {
    PERMISSION_GRANTED, PERMISSION_RATIONALE, PERMISSION_DENIED, INITIAL
}