package com.example.gatherersmap.presentation.main.ui.bottomsheet

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gatherersmap.data.ItemSpotRepositoryImpl
import com.example.gatherersmap.data.localdb.ItemSpotDatabase
import com.example.gatherersmap.data.network.ApiFactory
import com.example.gatherersmap.data.network.MushroomApi
import com.example.gatherersmap.navigation.BottomSheetScreenState
import com.example.gatherersmap.presentation.main.ui.PickLocationFab
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
                    localDataSource = ItemSpotDatabase.getDatabase(),
                    remoteDataSource = MushroomApi(ApiFactory.mushroomService)
                )
            )
        )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )
    val sheetScreenState by viewModel.sheetState.collectAsState(BottomSheetScreenState.Initial)
    val coroutineScope = rememberCoroutineScope()

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
                currentSheetState = sheetScreenState,
                scaffoldState = scaffoldState,
                coroutineScope = coroutineScope,
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            PickLocationFab(currentSheetState = sheetScreenState)
        }
    ) {
        MapScreen(
            onMapClick = {
                sheetScreenState.hideSheet(
                    scaffoldState = scaffoldState,
                    scope = coroutineScope
                )
                viewModel.onEvent(MapEvent.Initial)
            },
            onAddMarkerLongClick = {
                viewModel.onEvent(MapEvent.OnAddItemClick(it))
            },
            onMarkerClick = {
                viewModel.onEvent(MapEvent.OnDetailsItemClick(it))
            },
        )
    }
}