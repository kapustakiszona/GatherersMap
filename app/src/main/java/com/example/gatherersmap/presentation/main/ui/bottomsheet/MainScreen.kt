@file:OptIn(
    ExperimentalMaterialNavigationApi::class
)

package com.example.gatherersmap.presentation.main.ui.bottomsheet

import android.annotation.SuppressLint
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.gatherersmap.navigation.AppNavGraph
import com.example.gatherersmap.navigation.rememberNavigationState
import com.example.gatherersmap.presentation.main.ui.PickLocationFab
import com.example.gatherersmap.presentation.main.ui.map.MapScreen
import com.example.gatherersmap.presentation.main.vm.MapViewModel
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalPermissionsApi
@Composable
fun MainScreen(viewModel: MapViewModel) {
    val loadingState = viewModel.getAllLoading
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)
    val navigationState = rememberNavigationState(navController)

    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        sheetShape = RoundedCornerShape(
            topStart = 26.dp,
            topEnd = 26.dp
        ),
        sheetElevation = 20.dp,
        scrimColor = Color.Unspecified
    ) {
        Scaffold(
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                PickLocationFab(
                    navigationState = navigationState,
                    loadingState = loadingState,
                    addNewItemSpot = {
                        viewModel.setTemporalMarker(it)
                        navigationState.navigateToAddItem(it)
                    }
                )
            }
        )
        {
            AppNavGraph(
                navHostController = navController,
                mapScreenContent = {
                    MapScreen(
                        onMapClick = {
                            navigationState.navigateToMap()
                            viewModel.removeTemporalMarker()
                        },
                        onAddMarkerLongClick = { latLng ->
                            viewModel.setTemporalMarker(latLng)
                            navigationState.navigateToAddItem(latLng)
                        },
                        onMarkerClick = { itemSpot ->
                            navigationState.navigateToDetails(itemSpot)
                        },
                        viewModel = viewModel
                    )
                },
                addItemBottSheetContent = { newMarker ->
                    EditDetailsSheetContent(
                        itemSpot = newMarker,
                        onCancelClicked = {
                            navigationState.navigateToMap()
                            viewModel.removeTemporalMarker()
                        },
                        onSaveClicked = { itemSpot ->
                            coroutineScope.launch {
                                viewModel.insertItemSpot(itemSpot)
                                navigationState.navigateToDetails(itemSpot)
                            }
                        },
                        viewModel = viewModel
                    )
                },
                detailsItemBottSheetContent = { currentItemSpot ->
                    DetailsSheetContent(
                        itemSpot = currentItemSpot,
                        onEditClickListener = { itemSpot ->
                            navigationState.navigateToEditItem(itemSpot)
                        },
                        onDeleteClickListener = { itemSpot ->
                            coroutineScope.launch {
                                viewModel.deleteItemSpot(itemSpot)
                                navigationState.navigateToMap()
                            }
                        },
                        viewModel = viewModel
                    )
                },
                editItemBottSheetContent = {
                    EditDetailsSheetContent(
                        itemSpot = it,
                        onCancelClicked = { itemSpot ->
                            navigationState.navigateToDetails(itemSpot)
                        },
                        onSaveClicked = { itemSpot ->
                            coroutineScope.launch {
                                viewModel.updateItemSpot(itemSpot)
                                navigationState.navigateToDetails(itemSpot)
                            }
                        },
                        viewModel = viewModel
                    )
                }
            )
        }
    }
}
