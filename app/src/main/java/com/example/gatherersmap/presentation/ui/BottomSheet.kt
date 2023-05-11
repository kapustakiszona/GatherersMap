package com.example.gatherersmap.presentation.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gatherersmap.data.ItemSpotDatabase
import com.example.gatherersmap.data.ItemSpotRepositoryImpl
import com.example.gatherersmap.navigation.BottomSheetScreenState
import com.example.gatherersmap.presentation.vm.MapViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet(viewModel: MapViewModel) {
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val sheetScreenState = viewModel.sheetState.observeAsState(BottomSheetScreenState.Start)
    val coroutineScope = rememberCoroutineScope()
    BottomSheetScaffold(
        modifier = Modifier,
        scaffoldState = scaffoldState,
        sheetShape = RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp
        ),
        sheetPeekHeight = 10.dp,
        sheetElevation = 20.dp,
        floatingActionButton = {
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
            val scope = rememberCoroutineScope()
            when (val currentState = sheetScreenState.value) {
                is BottomSheetScreenState.Details -> {
                    DetailsSheetContent(itemSpot = currentState.itemSpot)
                    LaunchedEffect(
                        key1 = null,
                        block = {
                            scope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }
                        }
                    )
                }

                is BottomSheetScreenState.Edit -> {
                    EditDetailsSheetContent(
                        itemSpot = currentState.itemSpot,
                        repository = ItemSpotRepositoryImpl(database = ItemSpotDatabase),
                        viewModel = viewModel
                    )
                }

                BottomSheetScreenState.Start -> {

                }
            }
        }
    ) {
        MapScreen()
    }
}