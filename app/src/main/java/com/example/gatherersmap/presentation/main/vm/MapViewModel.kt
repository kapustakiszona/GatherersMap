package com.example.gatherersmap.presentation.main.vm

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gatherersmap.data.ItemSpotRepositoryImpl
import com.example.gatherersmap.data.datastore.DataStoreRepositoryImpl
import com.example.gatherersmap.data.datastore.DataStoreRepositoryImpl.PreferencesKeys.CAMERA_POSITION_LATITUDE
import com.example.gatherersmap.data.datastore.DataStoreRepositoryImpl.PreferencesKeys.CAMERA_POSITION_LONGITUDE
import com.example.gatherersmap.data.datastore.DataStoreRepositoryImpl.PreferencesKeys.CAMERA_POSITION_ZOOM
import com.example.gatherersmap.data.datastore.DataStoreRepositoryImpl.PreferencesKeys.PERMISSION_REQUEST_STATUS
import com.example.gatherersmap.data.network.dto.toItemSpot
import com.example.gatherersmap.data.network.dto.toListItemSpots
import com.example.gatherersmap.data.network.mapper.compareSpots
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.presentation.main.ui.MainActivity.Companion.TAG
import com.example.gatherersmap.presentation.main.ui.snackbar.SnackbarNetworkError
import com.example.gatherersmap.utils.NetworkResult
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: ItemSpotRepositoryImpl,
    private val dataStoreRepository: DataStoreRepositoryImpl
) : ViewModel() {

    private val _itemsState = MutableStateFlow(MapState())
    val itemsState = _itemsState.asStateFlow()

    private val _temporalMarker = MutableStateFlow<LatLng?>(null)
    val temporalMarker = _temporalMarker.asStateFlow()

    private val _networkErrorFlow = MutableSharedFlow<SnackbarNetworkError>()
    val networkErrorFlow = _networkErrorFlow.asSharedFlow()

    private val _splashVisibility = MutableStateFlow(true)
    val splashVisibility = _splashVisibility.asStateFlow()

    var getAllNetworkProgress by mutableStateOf(false)
    var deleteNetworkProgress by mutableStateOf(false)
    var insertAndUpdateNetworkProgress by mutableStateOf(false)

    var fabLocationVisibility by mutableStateOf(false)

    init {
        Log.d(TAG, "INIT wm: ")
        viewModelScope.launch(Dispatchers.IO) {
            getAllItemSpots()
            delay(2000)
            _splashVisibility.value = false
        }
    }

    private suspend fun getAllItemSpots() {
        withContext(Dispatchers.Main) {
            getAllNetworkProgress = true
        }
        withContext(Dispatchers.IO) {
            repository.getAllItemSpotsRemote()
                .collectLatest { result ->
                    when (result) {
                        is NetworkResult.Error -> {
                            setErrorMessage(
                                error = result.errorMessage,
                                action = {
                                    viewModelScope.launch {
                                        getAllItemSpots()
                                    }
                                }
                            )
                        }

                        is NetworkResult.Success -> {
                            _itemsState.update {
                                it.copy(itemSpots = result.data.toListItemSpots())
                            }
                        }
                    }
                }
            getAllNetworkProgress = false
        }
    }

    suspend fun insertItemSpot(itemSpot: ItemSpot) {
        insertAndUpdateNetworkProgress = true
        try {
            when (val result = repository.insertItemSpotRemote(itemSpot)) {
                is NetworkResult.Error -> {
                    repository.insertItemSpotLocal(spot = itemSpot)
                    setErrorMessage(
                        error = result.errorMessage,
                        action = {
                            viewModelScope.launch {
                                insertItemSpot(itemSpot)
                            }
                        }
                    )
                }

                is NetworkResult.Success -> {
                    val newItemId = result.data.itemId
                    if (newItemId != null) {
                        getItemAndUpdateList(newItemId)
                        removeTemporalMarker()
                    }
                }
            }
        } finally {
            insertAndUpdateNetworkProgress = false
        }
    }

    suspend fun deleteItemSpot(itemSpotId: Int) {
        deleteNetworkProgress = true
        try {
            when (val result = repository.deleteItemSpotRemote(itemSpotId)) {
                is NetworkResult.Error -> {
                    setErrorMessage(
                        error = result.errorMessage,
                        action = {
                            viewModelScope.launch { deleteItemSpot(itemSpotId) }
                        }
                    )
                }

                is NetworkResult.Success -> {
                    val isSuccess = result.data.isSuccess
                    _itemsState.update { state ->
                        state.copy(
                            itemSpots = state.itemSpots.filter { it.id != itemSpotId }
                        )
                    }
                }
            }
        } finally {
            deleteNetworkProgress = false
        }
    }

    suspend fun updateItemSpot(oldSpot: ItemSpot, newSpot: ItemSpot) {
        insertAndUpdateNetworkProgress = true
        withContext(Dispatchers.IO) {
            val updatedSpot = compareSpots(oldSpot = oldSpot, newSpot = newSpot)
            try {
                when (val result = repository.updateItemSpotDetailsRemote(updatedSpot)) {
                    is NetworkResult.Error -> {
                        setErrorMessage(
                            error = result.errorMessage,
                            action = {
                                viewModelScope.launch {
                                    updateItemSpot(oldSpot = oldSpot, newSpot = newSpot)
                                }
                            }
                        )
                    }

                    is NetworkResult.Success -> {
                        getItemAndUpdateList(oldSpot.id)
                    }
                }
            } finally {
                insertAndUpdateNetworkProgress = false
            }
        }
    }

    private suspend fun getItemAndUpdateList(itemSpotId: Int) {
        withContext(Dispatchers.IO) {
            when (val result = repository.getItemSpotRemote(spotId = itemSpotId)) {
                is NetworkResult.Error -> {
                    setErrorMessage(
                        error = result.errorMessage,
                        action = {
                            viewModelScope.launch {
                                getItemAndUpdateList(itemSpotId)
                            }
                        }
                    )
                }

                is NetworkResult.Success -> {
                    val item = result.data.mushroom?.toItemSpot()
                    item?.let {
                        _itemsState.update { state ->
                            val updatedList = state.itemSpots.toMutableList()
                            val existingItemIndex = updatedList.indexOfFirst { it.id == item.id }
                            if (existingItemIndex != -1) {
                                updatedList[existingItemIndex] = item
                            } else {
                                updatedList.add(item)
                            }
                            MapState(updatedList)
                        }
                    }
                }
            }
        }
    }

    fun savePermissionRequestStatus(hasInitialRequest: Boolean) {
        viewModelScope.launch {
            dataStoreRepository.putBoolean(
                key = PERMISSION_REQUEST_STATUS,
                value = hasInitialRequest
            )
        }
    }

    fun getPermissionRequestStatus(): Boolean = runBlocking {
        dataStoreRepository.getBoolean(PERMISSION_REQUEST_STATUS) ?: false
    }

    fun saveCameraPosition(cameraPosition: CameraPosition) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.putDouble(
                key = CAMERA_POSITION_LATITUDE,
                value = cameraPosition.target.latitude
            )
            dataStoreRepository.putDouble(
                key = CAMERA_POSITION_LONGITUDE,
                value = cameraPosition.target.longitude
            )
            dataStoreRepository.putFloat(
                key = CAMERA_POSITION_ZOOM,
                value = cameraPosition.zoom
            )
        }
    }

    fun getCameraPosition(): CameraPosition = runBlocking {
        CameraPosition.fromLatLngZoom(
            LatLng(
                dataStoreRepository.getDouble(CAMERA_POSITION_LATITUDE) ?: Random.nextDouble(),
                dataStoreRepository.getDouble(CAMERA_POSITION_LONGITUDE) ?: Random.nextDouble()
            ),
            dataStoreRepository.getFloat(CAMERA_POSITION_ZOOM) ?: 0f
        )
    }

    private fun setErrorMessage(error: String, action: () -> Unit) {
        viewModelScope.launch {
            val errorData = SnackbarNetworkError(error, action)
            _networkErrorFlow.emit(errorData)
        }
    }

    fun setFabVisibility(isItGoogleMapRoute: Boolean) {
        fabLocationVisibility = isItGoogleMapRoute
    }

    fun setTemporalMarker(latLng: LatLng) {
        _temporalMarker.update { latLng }
    }

    fun removeTemporalMarker() {
        _temporalMarker.update { null }
    }

}