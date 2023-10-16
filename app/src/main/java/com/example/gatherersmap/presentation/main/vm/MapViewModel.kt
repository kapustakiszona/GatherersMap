package com.example.gatherersmap.presentation.main.vm

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gatherersmap.data.ItemSpotRepositoryImpl
import com.example.gatherersmap.data.network.mapper.compareSpots
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
        viewModelScope.launch(Dispatchers.IO) {
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

    private suspend fun getAllItemSpots() {
        withContext(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                getAllNetworkProgress = true
            }
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
                        getItemSpot(newItemId)
                        val newItem = _itemsState.value.itemSpots.find {
                            it.id == newItemId
                        }
                        _navigationDestination.update {
                            NavigationDestinations.Details(newItem ?: itemSpot)
                        }
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
        } finally {
            deleteNetworkProgress = false
        }
    }

    suspend fun updateItemSpot(oldSpot: ItemSpot, newSpot: ItemSpot) {
        insertAndUpdateNetworkProgress = true
        withContext(Dispatchers.IO) {
            val updatedSpot = compareSpots(oldSpot = oldSpot, newSpot = newSpot)
            delay(2000)
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
                        getItemSpot(oldSpot.id)
                        val updatedItemSpot =
                            itemsState.value.itemSpots.find {
                                (it.id == oldSpot.id)
                            }
                        if (updatedItemSpot != null) {
                            _navigationDestination.update {
                                NavigationDestinations.Details(updatedItemSpot)
                            }
                        }
                    }
                }
            } finally {
                insertAndUpdateNetworkProgress = false
            }
        }
    }

    private suspend fun getItemSpot(itemSpotId: Int) {
        withContext(Dispatchers.IO) {
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
                    if (item != null) {
                        val oldList = itemsState.value.itemSpots.toMutableList()
                        val existItem = oldList.find {
                            it.id == item.id
                        }
                        if (existItem == null) {
                            oldList.add(item)
                            _itemsState.update { MapState(oldList) }
                        } else {
                            val newList = oldList.map {
                                if (it.id == existItem.id) {
                                    val a = it.copy(
                                        name = item.name,
                                        description = item.description,
                                        image = item.image
                                    )
                                    a
                                } else {
                                    it
                                }
                            }
                            _itemsState.update { MapState(newList) }
                        }
                    }
                }
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