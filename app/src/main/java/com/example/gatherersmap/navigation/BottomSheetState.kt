package com.example.gatherersmap.navigation

sealed class BottomSheetState() {

    object Start : BottomSheetState()

    object NewMarker : BottomSheetState()

    object Details : BottomSheetState()

}
