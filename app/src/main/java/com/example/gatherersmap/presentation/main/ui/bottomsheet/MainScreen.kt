@file:OptIn(
    ExperimentalMaterialNavigationApi::class
)

package com.example.gatherersmap.presentation.main.ui.bottomsheet

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gatherersmap.navigation.AppNavGraph
import com.example.gatherersmap.navigation.ScreenState
import com.example.gatherersmap.navigation.rememberNavigationState
import com.example.gatherersmap.presentation.main.ui.map.MapScreen
import com.example.gatherersmap.presentation.main.ui.map.PickLocationFab
import com.example.gatherersmap.presentation.main.ui.snackbar.SnackBarNetworkErrorManager
import com.example.gatherersmap.presentation.main.vm.MapViewModel
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialNavigationApi::class)
@ExperimentalPermissionsApi
@Composable
fun MainScreen(viewModel: MapViewModel = hiltViewModel()) {
    val loadingState = viewModel.getAllNetworkProgress
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)
    val navigationState = rememberNavigationState(navController)
    val snackbarHostState = remember { SnackbarHostState() }
    val navBackStackEntry = navigationState.navHostController.currentBackStackEntryAsState()


    if (navBackStackEntry.value?.destination?.route == ScreenState.GoogleMap.route) viewModel.removeTemporalMarker()

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            PickLocationFab(
                navBackStackEntry = navBackStackEntry,
                loadingState = loadingState,
                addNewItemSpot = { latLng ->
                    viewModel.setTemporalMarker(latLng)
                    navigationState.navigateToAddItem(latLng)
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    )
    {
        ModalBottomSheetLayout(
            modifier = Modifier.padding(it),
            bottomSheetNavigator = bottomSheetNavigator,
            sheetShape = RoundedCornerShape(
                topStart = 26.dp,
                topEnd = 26.dp
            ),
            sheetElevation = 20.dp,
            scrimColor = Color.Unspecified
        )
        {
            SnackBarNetworkErrorManager(
                networkErrorFlow = viewModel.networkErrorFlow,
                snackbarHostState = snackbarHostState
            )
            AppNavGraph(
                navHostController = navController,
                mapScreenContent = {
                    MapScreen(
                        onMapClick = {
                            navigationState.navigateToMap()
                        },
                        onAddMarkerLongClick = { latLng ->
                            navigationState.navigateToAddItem(latLng)
                            viewModel.setTemporalMarker(latLng)
                        },
                        onMarkerClick = { itemSpot ->
                            viewModel.removeTemporalMarker()
                            navigationState.navigateToDetails(itemSpot)
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
                                viewModel.deleteItemSpot(itemSpotId = itemSpot.id)
                                navigationState.navigateToMap()
                            }
                        },
                        onImageClick = { itemSpot ->
                            navigationState.navigateToDetailsImage(itemSpot)
                        },
                        deleteProgress = viewModel.deleteNetworkProgress
                    )
                },
                addItemBottSheetContent = { newMarker ->
                    EditDetailsSheetContent(
                        itemSpot = newMarker,
                        onCancelClicked = {
                            navController.popBackStack()
                        },
                        onSaveClicked = { itemSpot ->
                            coroutineScope.launch {
                                viewModel.insertItemSpot(itemSpot)
                                navigationState.navigateToDetails(viewModel.itemsState.value.itemSpots.last())
                            }
                        },
                        insertAndUpdateNetworkProgress = viewModel.insertAndUpdateNetworkProgress,
                    )
                },
                editItemBottSheetContent = { currentItem ->
                    EditDetailsSheetContent(
                        itemSpot = currentItem,
                        onCancelClicked = { itemSpot ->
                            navController.popBackStack()
                        },
                        onSaveClicked = { editedItemSpot ->
                            coroutineScope.launch {
                                viewModel.updateItemSpot(
                                    oldSpot = currentItem,
                                    newSpot = editedItemSpot
                                )
                                navigationState.navigateToDetails(
                                    itemSpot = viewModel.itemsState.value.itemSpots.find { item ->
                                        item.id == currentItem.id
                                    } ?: return@launch
                                )
                            }
                        },
                        insertAndUpdateNetworkProgress = viewModel.insertAndUpdateNetworkProgress,
                    )
                },
                detailsImageContent = { itemSpot ->
                    ImageViewerComponent(
                        itemSpot = itemSpot,
                        backClick = {
                            navigationState.navigateBackFromImage(itemSpot)
                        })
                }
            )
        }
    }
}