package com.example.gatherersmap.presentation.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gatherersmap.data.ItemSpotRepositoryImpl

class MapViewModelFactory(private val repositoryImpl: ItemSpotRepositoryImpl) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapViewModel(repository = repositoryImpl) as T
    }
}