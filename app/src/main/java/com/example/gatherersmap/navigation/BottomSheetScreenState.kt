package com.example.gatherersmap.navigation

import com.example.gatherersmap.domain.model.ItemSpot

sealed class BottomSheetScreenState() {

    object Start : BottomSheetScreenState()

    data class Edit(val itemSpot: ItemSpot) : BottomSheetScreenState()

    data class Details(val itemSpot: ItemSpot) : BottomSheetScreenState()

}
