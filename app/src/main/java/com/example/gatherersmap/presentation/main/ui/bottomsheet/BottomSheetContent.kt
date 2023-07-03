@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class)

package com.example.gatherersmap.presentation.main.ui.bottomsheet

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gatherersmap.navigation.BottomSheetScreenState
import com.example.gatherersmap.presentation.main.ui.map.MapEvent
import com.example.gatherersmap.presentation.main.vm.MapViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun BottomSheetContent(
    currentSheetState: BottomSheetScreenState,
    scaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope,
    viewModel: MapViewModel = viewModel(),
) {
    when (currentSheetState) {
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
                    viewModel.insertItemSpot(itemSpot)
                    currentSheetState.hideSheet(
                        scope = coroutineScope,
                        scaffoldState = scaffoldState
                    )
                    viewModel.onEvent(MapEvent.Initial)
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
                    viewModel.updateItemSpot(itemSpot)
                    currentSheetState.hideSheet(
                        scope = coroutineScope,
                        scaffoldState = scaffoldState
                    )
                    viewModel.onEvent(MapEvent.OnDetailsItemClick(itemSpot))
                }
            )
        }

        BottomSheetScreenState.Initial -> {
        }
    }
}