package com.example.gatherersmap.presentation.ui.bottomsheet

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gatherersmap.data.ItemSpotDatabase
import com.example.gatherersmap.data.ItemSpotRepositoryImpl
import com.example.gatherersmap.navigation.BottomSheetScreenState
import com.example.gatherersmap.presentation.ui.map.MapEvent
import com.example.gatherersmap.presentation.ui.map.MapScreen
import com.example.gatherersmap.presentation.vm.MapViewModel
import com.example.gatherersmap.presentation.vm.MapViewModelFactory
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet() {
    val viewModel: MapViewModel =
        viewModel(
            factory = MapViewModelFactory(
                ItemSpotRepositoryImpl(
                    ItemSpotDatabase
                )
            )
        )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val itemsState = viewModel.itemsState.collectAsState()
    val sheetScreenState =
        viewModel.sheetState.collectAsState(BottomSheetScreenState.Initial)
    val tempSpotState = viewModel.temporalMarker.collectAsState()
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
        floatingActionButton = {
            // TODO: button for test
            DefiningPositionFab(
                onFabClickListener = {
                    coroutineScope.launch {
                        if (scaffoldState.bottomSheetState.isCollapsed) {
                            scaffoldState.bottomSheetState.expand()
                        } else {
                            scaffoldState.bottomSheetState.collapse()
                        }
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        sheetContent = {

            when (val currentSheetState = sheetScreenState.value) {
// TODO: добавить анимацию перехода с детейл стейта на эдит при лонгклике
                is BottomSheetScreenState.Add -> {
                    currentSheetState.showSheet(
                        scope = coroutineScope,
                        scaffoldState = scaffoldState
                    )
                    EditDetailsSheetContent(
                        itemSpot = currentSheetState.itemSpot,
                        onCancelClicked = {
                            viewModel.onEvent(MapEvent.Initial)
                            currentSheetState.hideSheet(
                                scope = coroutineScope,
                                scaffoldState = scaffoldState
                            )
                        },
                        onSaveClicked = { itemSpot ->
                            if (itemSpot.id == 0) {
                                viewModel.insertItemSpot(itemSpot)
                            } else {
                                viewModel.updateItemSpot(itemSpot)
                            }
                            viewModel.onEvent(MapEvent.Initial)
                            currentSheetState.hideSheet(
                                scope = coroutineScope,
                                scaffoldState = scaffoldState
                            )
                        }
                    )
                }

                is BottomSheetScreenState.Details -> {
                    currentSheetState.showSheet(
                        scope = coroutineScope,
                        scaffoldState = scaffoldState
                    )
                    DetailsSheetContent(
                        itemSpot = currentSheetState.itemSpot,
                        onEditClickListener = {
                            viewModel.onEvent(MapEvent.OnEditItemClick(it))
                            currentSheetState.hideSheet(
                                scope = coroutineScope,
                                scaffoldState = scaffoldState
                            )
                        },
                        onDeleteClickListener = {
                            viewModel.onEvent(MapEvent.OnDeleteItemClick(it))
                            currentSheetState.hideSheet(
                                scope = coroutineScope,
                                scaffoldState = scaffoldState
                            )
                        }
                    )
                }

                is BottomSheetScreenState.Edit -> {
                    currentSheetState.showSheet(
                        scope = coroutineScope,
                        scaffoldState = scaffoldState
                    )
                    EditDetailsSheetContent(
                        itemSpot = currentSheetState.itemSpot,
                        onCancelClicked = {
                            viewModel.onEvent(MapEvent.OnDetailsItemClick(it))
                            currentSheetState.hideSheet(
                                scope = coroutineScope,
                                scaffoldState = scaffoldState
                            )
                        },
                        onSaveClicked = { itemSpot ->
                            if (itemSpot.id == 0) {
                                viewModel.insertItemSpot(itemSpot)
                            } else {
                                viewModel.updateItemSpot(itemSpot)
                            }
                            viewModel.onEvent(MapEvent.OnDetailsItemClick(itemSpot))
                            currentSheetState.hideSheet(
                                scope = coroutineScope,
                                scaffoldState = scaffoldState
                            )
                        }
                    )
                }

                BottomSheetScreenState.Initial -> {
                }
            }
        }
    ) {
        MapScreen(
            itemSpots = itemsState,
            temporalSpot = tempSpotState,
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
            }
        )
    }
}