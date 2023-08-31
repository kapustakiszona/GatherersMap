@file:OptIn(
    ExperimentalMaterialNavigationApi::class
)

package com.example.gatherersmap.presentation.main.ui.bottomsheet

import android.annotation.SuppressLint
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.gatherersmap.navigation.AppNavGraph
import com.example.gatherersmap.navigation.NavigationDestinations
import com.example.gatherersmap.navigation.NavigationHandler
import com.example.gatherersmap.navigation.rememberNavigationState
import com.example.gatherersmap.presentation.main.ui.PickLocationFab
import com.example.gatherersmap.presentation.main.ui.map.MapScreen
import com.example.gatherersmap.presentation.main.ui.snackbar.SnackBarNetworkErrorManager
import com.example.gatherersmap.presentation.main.vm.MapViewModel
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialNavigationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalPermissionsApi
@Composable
fun MainScreen(viewModel: MapViewModel) {
    val loadingState = viewModel.getAllLoading
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)
    val navigationState = rememberNavigationState(navController)
    val snackbarHostState = remember { SnackbarHostState() }
    val navDest = viewModel.navigationDestination.collectAsState()
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
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    )
    {
        ModalBottomSheetLayout(
            modifier = Modifier,
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
            NavigationHandler(
                navigationDestination = navDest.value,
                navigationState = navigationState,
            )
            AppNavGraph(
                navHostController = navController,
                mapScreenContent = {
                    MapScreen(
                        onMapClick = {
                            viewModel.setNavigationDestination(NavigationDestinations.Map)
                            viewModel.removeTemporalMarker()
                        },
                        onAddMarkerLongClick = { latLng ->
                            viewModel.setTemporalMarker(latLng)
                            viewModel.setNavigationDestination(NavigationDestinations.Add(latLng))
                        },
                        onMarkerClick = { itemSpot ->
                            viewModel.setNavigationDestination(
                                NavigationDestinations.Details(
                                    itemSpot
                                )
                            )
                        },
                        viewModel = viewModel
                    )
                },
                detailsItemBottSheetContent = { currentItemSpot ->
                    DetailsSheetContent(
                        itemSpot = currentItemSpot,
                        onEditClickListener = { itemSpot ->
                            viewModel.setNavigationDestination(NavigationDestinations.Edit(itemSpot))
                        },
                        onDeleteClickListener = { itemSpot ->
                            coroutineScope.launch {
                                viewModel.deleteItemSpot(itemSpot)
                            }
                        },
                        viewModel = viewModel
                    )
                },
                addItemBottSheetContent = { newMarker ->
                    EditDetailsSheetContent(
                        itemSpot = newMarker,
                        onCancelClicked = {
                            // TODO: удалить темпМаркер при нажатии "назад"
                            viewModel.setNavigationDestination(NavigationDestinations.Map)
                            viewModel.removeTemporalMarker()
                        },
                        onSaveClicked = { itemSpot ->
                            coroutineScope.launch {
                                viewModel.insertItemSpot(itemSpot)
                            }
                        },
                        viewModel = viewModel
                    )
                },
                editItemBottSheetContent = {
                    EditDetailsSheetContent(
                        itemSpot = it,
                        onCancelClicked = { itemSpot ->
                            viewModel.setNavigationDestination(
                                NavigationDestinations.Details(
                                    itemSpot
                                )
                            )
                        },
                        onSaveClicked = { itemSpot ->
                            coroutineScope.launch {
                                // TODO: navigate to details
                                viewModel.updateItemSpot(itemSpot)
                            }
                        },
                        viewModel = viewModel
                    )
                }
            )
        }
    }
}