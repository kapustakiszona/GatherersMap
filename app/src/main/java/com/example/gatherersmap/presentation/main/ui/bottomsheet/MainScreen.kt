package com.example.gatherersmap.presentation.main.ui.bottomsheet

import android.util.Log
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gatherersmap.data.ItemSpotDatabase
import com.example.gatherersmap.data.ItemSpotRepositoryImpl
import com.example.gatherersmap.navigation.BottomSheetScreenState
import com.example.gatherersmap.presentation.main.ui.MainActivity.Companion.TAG
import com.example.gatherersmap.presentation.main.ui.map.MapEvent
import com.example.gatherersmap.presentation.main.ui.map.MapScreen
import com.example.gatherersmap.presentation.main.vm.MapViewModel
import com.example.gatherersmap.presentation.main.vm.MapViewModelFactory
import com.google.accompanist.permissions.ExperimentalPermissionsApi


@ExperimentalPermissionsApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen() {
    val viewModel: MapViewModel =
        viewModel(
            factory = MapViewModelFactory(
                ItemSpotRepositoryImpl(
                    ItemSpotDatabase
                )
            )
        )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )
    val sheetScreenState =
        viewModel.sheetState.collectAsState(BottomSheetScreenState.Initial)
    val snackBarHostState = scaffoldState.snackbarHostState
    val coroutineScope = rememberCoroutineScope()
    var isFabClicked by remember { mutableStateOf(false) }

    BottomSheetScaffold(
        modifier = Modifier,
        scaffoldState = scaffoldState,
        sheetShape = RoundedCornerShape(
            topStart = 26.dp,
            topEnd = 26.dp
        ),
        sheetPeekHeight = 0.dp,
        sheetElevation = 20.dp,
        sheetContent = {
            BottomSheetContent(
                currentSheetState = sheetScreenState.value,
                scaffoldState = scaffoldState,
                coroutineScope = coroutineScope,
            )
        }
    ) {
        MapScreen(
            onMapClick = {
                sheetScreenState.value.hideSheet(
                    scaffoldState = scaffoldState,
                    scope = coroutineScope
                )
                viewModel.onEvent(MapEvent.Initial)
            },
            onAddMarkerLongClick = {
                viewModel.onEvent(MapEvent.OnAddItemLongClick(it))
            },
            onMarkerClick = {
                viewModel.onEvent(MapEvent.OnDetailsItemClick(it))
            },
            scaffoldState = scaffoldState
        )
    }
}