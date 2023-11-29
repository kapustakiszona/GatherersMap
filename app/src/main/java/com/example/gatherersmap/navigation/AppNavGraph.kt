package com.example.gatherersmap.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.navigation.ScreenState.Companion.KEY_ITEM_SPOT
import com.example.gatherersmap.navigation.ScreenState.Companion.KEY_LAT_LNG_MARKER
import com.example.gatherersmap.presentation.main.ui.MainActivity.Companion.TAG
import com.example.gatherersmap.presentation.main.vm.MapViewModel
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    mapScreenContent: @Composable (MapViewModel) -> Unit,
    addItemBottSheetContent: @Composable (ItemSpot) -> Unit,
    detailsItemBottSheetContent: @Composable (ItemSpot) -> Unit,
    editItemBottSheetContent: @Composable (ItemSpot) -> Unit,
) {
    NavHost(
        navController = navHostController,
        startDestination = ScreenState.GoogleMap.route,
    ) {
        Log.d(TAG, "AppNavGraph: ${navHostController.currentBackStackEntry?.destination}")
        composable(route = ScreenState.GoogleMap.route) {
            mapScreenContent(hiltViewModel())
        }
        navigation(
            startDestination = ScreenState.GoogleMap.route,
            route = ScreenState.BottSheetHome.route
        ) {
            bottomSheet(
                route = ScreenState.BottSheetHome.Add.route,
                arguments = listOf(
                    navArgument(
                        name = KEY_LAT_LNG_MARKER
                    ) {
                        type = NavType.StringType
                    }
                )
            ) {
                val newMarkerJson =
                    it.arguments?.getString(KEY_LAT_LNG_MARKER) ?: ""
                val newMarker = Json.decodeFromString<ItemSpot>(newMarkerJson)
                addItemBottSheetContent(newMarker)
            }
            bottomSheet(
                route = ScreenState.BottSheetHome.Details.route,
                arguments = listOf(
                    navArgument(
                        name = KEY_ITEM_SPOT
                    ) {
                        type = NavType.StringType
                    })
            ) {
                val itemSpotJson =
                    it.arguments?.getString(KEY_ITEM_SPOT) ?: ""
                val itemSpot = Json.decodeFromString<ItemSpot>(itemSpotJson)
                detailsItemBottSheetContent(itemSpot)
            }

            bottomSheet(
                route = ScreenState.BottSheetHome.Edit.route,
                arguments = listOf(
                    navArgument(
                        name = KEY_ITEM_SPOT
                    ) {
                        type = NavType.StringType
                    }
                )
            ) {
                val itemSpotJson =
                    it.arguments?.getString(KEY_ITEM_SPOT) ?: ""
                val itemSpot = Json.decodeFromString<ItemSpot>(itemSpotJson)
                Log.d(TAG, "AppNavGraph: itemspot -> ${itemSpot.name}")
                editItemBottSheetContent(itemSpot)
            }
        }

    }
}