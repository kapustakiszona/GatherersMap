@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterialNavigationApi::class
)

package com.example.gatherersmap.presentation.main.ui.bottomsheet

import android.annotation.SuppressLint
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.delay


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalPermissionsApi
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
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false,
        )
    )
    val sheetScreenState by viewModel.sheetState.collectAsState(BottomSheetScreenState.Initial)
    val coroutineScope = rememberCoroutineScope()
    val loadingState = viewModel.getAllLoading

    BottomSheetScaffold(
        modifier = Modifier,
        scaffoldState = scaffoldState,
        sheetShape = RoundedCornerShape(
            topStart = 26.dp,
            topEnd = 26.dp
        ),
        sheetPeekHeight = 0.dp,
        sheetTonalElevation = 20.dp,
        sheetShadowElevation = 20.dp,
        sheetContent = {
            BottomSheetContent(
                currentSheetState = sheetScreenState,
                scaffoldState = scaffoldState,
                coroutineScope = coroutineScope,
            )
        },
        sheetDragHandle = {}
    ) {
        Scaffold(
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                PickLocationFab(
                    currentSheetState = sheetScreenState,
                    loadingState = loadingState
                )
            }
        ) {
            MapScreen(
                onMapClick = {
                    viewModel.setVisibilities(BottomSheetVisibility.HIDE)
                    viewModel.onEvent(MapEvent.Initial)
                },
                onAddMarkerLongClick = {
                    viewModel.setVisibilities(BottomSheetVisibility.HIDE)
                    viewModel.onEvent(MapEvent.OnAddItemClick(it))
                },
                onMarkerClick = {
                    viewModel.onEvent(MapEvent.OnDetailsItemClick(it))
                },
            )
        }
        val visibility by viewModel.sheetVisibleState.collectAsState()
        SheetVisibilityHandler(
            scaffoldState = scaffoldState,
            visibility = visibility
        )
    }
}

@Composable
private fun SheetVisibilityHandler(
    scaffoldState: BottomSheetScaffoldState,
    visibility: BottomSheetVisibility,
) {
    LaunchedEffect(key1 = visibility) {
        when (visibility) {
            BottomSheetVisibility.HIDE -> {
                scaffoldState.bottomSheetState.hide()
                delay(250)
            }

            BottomSheetVisibility.SHOW -> {
                scaffoldState.bottomSheetState.expand()
            }

            BottomSheetVisibility.INITIAL -> {}
        }
    }
}