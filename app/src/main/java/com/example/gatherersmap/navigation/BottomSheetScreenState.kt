@file:OptIn(
    ExperimentalMaterialApi::class
)

package com.example.gatherersmap.navigation

import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import com.example.gatherersmap.domain.model.ItemSpot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed class BottomSheetScreenState() {

    object Start : BottomSheetScreenState()

    data class Edit(val itemSpot: ItemSpot) : BottomSheetScreenState()

    data class Details(val itemSpot: ItemSpot) : BottomSheetScreenState()

    fun showSheet(scaffoldState: BottomSheetScaffoldState, scope: CoroutineScope) {
        scope.launch {
            scaffoldState.bottomSheetState.expand()
        }
    }

    fun hideSheet(scaffoldState: BottomSheetScaffoldState, scope: CoroutineScope){
        scope.launch {
            scaffoldState.bottomSheetState.collapse()
        }
    }
}
