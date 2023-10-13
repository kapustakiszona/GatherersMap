package com.example.gatherersmap.presentation.main.vm

import android.util.Log
import androidx.compose.runtime.collectAsState
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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.withContext
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

    var getAllNetworkProgress by mutableStateOf(false)
    var deleteNetworkProgress by mutableStateOf(false)
    var insertAndUpdateNetworkProgress by mutableStateOf(false)

    init {
        Log.d(TAG, "INIT wm: ")
        viewModelScope.launch {
            getAllItemSpots()
        }
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
            getAllNetworkProgress = true
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
                                MapState(itemSpots = result.data.toListItemSpots())
                                // it.copy(itemSpots = result.data.toListItemSpots())
                            }
                        }
                    }
                }
            getAllNetworkProgress = false
        }
    }

    suspend fun insertItemSpot(itemSpot: ItemSpot) {
        insertAndUpdateNetworkProgress = true
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
                getAllItemSpots()
                val newItemId = result.data.itemId
                //getItemSpot(newItemId ?: 0)
                val newItem = _itemsState.value.itemSpots.find {
                    it.id == newItemId
                }
                Log.d(TAG, "insertItemSpot: newItem->$newItem")
                _navigationDestination.update {
                    NavigationDestinations.Details(newItem ?: itemSpot)
                }
                removeTemporalMarker()
                // getAllItemSpots()
            }
        }
        insertAndUpdateNetworkProgress = false
    }

    suspend fun deleteItemSpot(itemSpotId: Int) {
        deleteNetworkProgress = true
        when (val result = repository.deleteItemSpotRemote(itemSpotId)) {
            is NetworkResult.Error -> {
                setErrorMessage(
                    error = result.errorMessage,
                    action = {
                        viewModelScope.launch {
                            deleteItemSpot(itemSpotId)
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
        deleteNetworkProgress = false
    }

    suspend fun updateItemSpot(editedItemSpot: EditedItemSpot) {
        insertAndUpdateNetworkProgress = true
        try {
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
                    getAllItemSpots().also { Log.d(TAG, "updateItemSpot: getAll running") }
                    val updatedItemSpot: ItemSpot =
                        itemsState.value.itemSpots.find {
                            (it.id == editedItemSpot.id)
                        } ?: editedItemSpot.toItemSpot()

                    Log.d(TAG, "updateItemSpot: UPDspot ->>>>> $updatedItemSpot")
                    _navigationDestination.update {
                        NavigationDestinations.Details(updatedItemSpot)
                    }
                }
            }
        } finally {
            insertAndUpdateNetworkProgress = false
        }
    }

    suspend fun getItemSpot(itemSpotId: Int) {
        when (val result = repository.getItemSpotRemote(spotId = itemSpotId)) {
            is NetworkResult.Error -> {
                setErrorMessage(
                    error = result.errorMessage,
                    action = {
                        viewModelScope.launch {
                            getItemSpot(itemSpotId)
                        }
                    }
                )
            }

            is NetworkResult.Success -> {
                val item = result.data.mushroom?.toItemSpot()
                val oldList = itemsState.value.itemSpots.toMutableList().also {
                    Log.d(TAG, "getItemSpot: before map ${it.size}")
                }
                val isExist = oldList.find {
                    it.id == item!!.id
                }
                if (isExist == null) {
                    oldList.add(item!!)
                } else {
                    oldList.map {
                        if (it.id == isExist.id) {
                            val a = it.copy(
                                name = item?.name.orEmpty(),
                                description = item?.description.orEmpty(),
                                image = item?.image.orEmpty()
                            ).also { newItem -> Log.d(TAG, "getItemSpot: replaced -> $newItem") }
                            a
                        } else {
                            it
                        }
                    }
                }
                _itemsState.update { MapState(oldList) }
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