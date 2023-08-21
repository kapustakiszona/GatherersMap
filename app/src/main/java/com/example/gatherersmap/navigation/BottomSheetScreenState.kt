@file:OptIn(
    ExperimentalMaterial3Api::class
)

package com.example.gatherersmap.navigation

import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.gatherersmap.domain.model.ItemSpot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

sealed class BottomSheetScreenState {

    object Initial : BottomSheetScreenState()
    data class Add(val itemSpot: ItemSpot) : BottomSheetScreenState()

    data class Edit(val itemSpot: ItemSpot) : BottomSheetScreenState()

    data class Details(val itemSpot: ItemSpot) : BottomSheetScreenState()


    fun showSheet(
        scaffoldState: BottomSheetScaffoldState,
        scope: CoroutineScope,
    ) {
        scope.launch {
            delay(200)
            scaffoldState.bottomSheetState.expand()
        }
    }

    fun hideSheet(
        scaffoldState: BottomSheetScaffoldState,
        scope: CoroutineScope,
    ) {
        scope.launch {
            scaffoldState.bottomSheetState.hide()
        }
    }
}
