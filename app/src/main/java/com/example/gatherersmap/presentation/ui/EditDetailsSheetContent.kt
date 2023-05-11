package com.example.gatherersmap.presentation.ui

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewModelScope
import com.example.gatherersmap.data.ItemSpotRepositoryImpl
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.presentation.vm.MapViewModel
import kotlinx.coroutines.launch

@Composable
fun EditDetailsSheetContent(
    itemSpot: ItemSpot,
    repository: ItemSpotRepositoryImpl,
    viewModel: MapViewModel
) {
    var newItem = itemSpot

    TextField(
        value = "",
        onValueChange = { newName ->
            newItem = newItem.copy(name = newName)
        },
        placeholder = {
            Text(text = itemSpot.name)
        }
    )
    TextField(
        value = "",
        onValueChange = { description ->
            newItem = newItem.copy(description = description)
        },
        placeholder = {
            Text(text = itemSpot.description)
        }
    )
    Button(onClick = {
        viewModel.viewModelScope.launch {
            repository.updateItemSpotDetails(newItem)
        }
    }
    ) {
        Text(text = "save")
    }
}