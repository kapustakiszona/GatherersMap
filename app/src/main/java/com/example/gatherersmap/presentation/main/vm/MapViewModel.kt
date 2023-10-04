package com.example.gatherersmap.presentation.main.vm

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gatherersmap.data.ItemSpotRepositoryImpl
import com.example.gatherersmap.data.network.mapper.EditedItemSpot
import com.example.gatherersmap.data.network.mapper.toItemSpot
import com.example.gatherersmap.data.network.mapper.toListItemSpots
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.navigation.NavigationDestinations
import com.example.gatherersmap.presentation.main.ui.MainActivity.Companion.TAG
import com.example.gatherersmap.presentation.main.ui.snackbar.SnackbarNetworkError
import com.example.gatherersmap.utils.NetworkResult
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
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: ItemSpotRepositoryImpl,
) : ViewModel() {

    private val _itemsState = MutableStateFlow(MapState())
    val itemsState = _itemsState.asStateFlow()

    private val _temporalMarker = MutableStateFlow<LatLng?>(null)
    val temporalMarker = _temporalMarker.asStateFlow()

    private val _navigationDestination =
        MutableStateFlow<NavigationDestinations>(NavigationDestinations.Current)
    val navigationDestination = _navigationDestination.asStateFlow()

    private val _networkErrorFlow = MutableSharedFlow<SnackbarNetworkError>()
    val networkErrorFlow = _networkErrorFlow.asSharedFlow()

    var insertLoading by mutableStateOf(false)
    var getAllLoading by mutableStateOf(false)
    var deleteLoading by mutableStateOf(false)
    var updateLoading by mutableStateOf(false)

    init {
        Log.d(TAG, "INIT wm: ")
        getAllItemSpots()
    }

    private fun setErrorMessage(error: String, action: () -> Unit) {
        viewModelScope.launch {
            val errorData = SnackbarNetworkError(error, action)
            _networkErrorFlow.emit(errorData)
        }
    }

    fun setNavigationDestination(navDest: NavigationDestinations) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_navigationDestination.value is NavigationDestinations.Add && navDest is NavigationDestinations.Details) {
                removeTemporalMarker()
            }// TODO: can be better?
            _navigationDestination.update {
                navDest
            }
        }
    }

    private fun getAllItemSpots() {
        viewModelScope.launch {
            getAllLoading = true
            repository.getAllItemSpotsRemote()
                .collectLatest { result ->
                    when (result) {
                        is NetworkResult.Error -> {
                            setErrorMessage(
                                error = result.errorMessage,
                                action = {
                                    getAllItemSpots()
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
            getAllLoading = false
        }
    }

    suspend fun insertItemSpot(itemSpot: ItemSpot) {
        insertLoading = true
        delay(1000)
        when (val result = repository.insertItemSpotRemote(itemSpot)) {
            is NetworkResult.Error -> {
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
                val fileName = result.data.fileName
                _navigationDestination.update {
                    NavigationDestinations.Details(itemSpot)
                }
                removeTemporalMarker()
                getAllItemSpots()
            }
        }
        insertLoading = false
    }

    suspend fun deleteItemSpot(itemSpot: ItemSpot) {
        deleteLoading = true
        when (val result = repository.deleteItemSpotRemote(itemSpot)) {
            is NetworkResult.Error -> {
                setErrorMessage(
                    error = result.errorMessage,
                    action = {
                        viewModelScope.launch {
                            deleteItemSpot(itemSpot)
                        }
                    }
                )
            }

            is NetworkResult.Success -> {
                val isSuccess = result.data.isSuccess
                _navigationDestination.update {
                    NavigationDestinations.Map
                }
                getAllItemSpots()
            }
        }
        deleteLoading = false
    }

    suspend fun updateItemSpot(editedItemSpot: EditedItemSpot) {
        updateLoading = true
        when (val result = repository.updateItemSpotDetailsRemote(editedItemSpot)) {
            is NetworkResult.Error -> {
                setErrorMessage(
                    error = result.errorMessage,
                    action = {
                        viewModelScope.launch {
                            updateItemSpot(editedItemSpot)
                        }
                    }
                )
            }

            is NetworkResult.Success -> {
                val isSuccess = result.data.isSuccess
                getItemSpot(editedItemSpot.toItemSpot())
                val updatedItemSpot = _itemsState.value.itemSpots.find {
                    (it.id == editedItemSpot.id)
                } ?: editedItemSpot.toItemSpot()
                _navigationDestination.update {
                    NavigationDestinations.Details(updatedItemSpot)
                }
            }
        }
        updateLoading = false
    }

    suspend fun getItemSpot(itemSpot: ItemSpot) {
        when (val result = repository.getItemSpotRemote(itemSpot)) {
            is NetworkResult.Error -> {
                setErrorMessage(
                    error = result.errorMessage,
                    action = {
                        viewModelScope.launch {
                            getItemSpot(itemSpot)
                        }
                    }
                )
            }

            is NetworkResult.Success -> {
                val item = result.data.mushroom?.toItemSpot() ?: itemSpot
                val oldList = _itemsState.value.itemSpots
                val updatedList = oldList.map {
                    if (it.id == item.id) {
                        item
                    } else {
                        it
                    }
                }
                _itemsState.update { MapState(updatedList) }
            }
        }
    }

    fun setTemporalMarker(latLng: LatLng) {
        _temporalMarker.update {
            latLng
        }
    }

    fun removeTemporalMarker() {
        _temporalMarker.update {
            null
        }
        // navigationDestination = NavigationDestinations.Map
    }

}