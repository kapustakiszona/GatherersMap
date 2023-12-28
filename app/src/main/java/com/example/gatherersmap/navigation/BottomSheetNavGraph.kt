package com.example.gatherersmap.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.gatherersmap.domain.model.ItemSpot
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.bottomSheetNavGraph(
    detailsItemBottSheetContent: @Composable (ItemSpot) -> Unit,
    detailsImageContent: @Composable (ItemSpot) -> Unit,
    addItemBottSheetContent: @Composable (ItemSpot) -> Unit,
    editItemBottSheetContent: @Composable (ItemSpot) -> Unit
) {
    navigation(
        startDestination = ScreenState.GoogleMap.route,
        route = ScreenState.BottomSheet.route
    ) {
        bottomSheet(
            route = ScreenState.BottomSheet.Details.route,
            arguments = listOf(
                navArgument(
                    name = ScreenState.KEY_ITEM_SPOT
                ) {
                    type = NavType.StringType
                })
        ) {
            val itemSpotJson =
                it.arguments?.getString(ScreenState.KEY_ITEM_SPOT) ?: ""
            val itemSpot = Json.decodeFromString<ItemSpot>(itemSpotJson)
            detailsItemBottSheetContent(itemSpot)
        }
        composable(
            route = ScreenState.BottomSheet.Image.route,
            arguments = listOf(
                navArgument(
                    name = ScreenState.KEY_ITEM_IMAGE
                ) {
                    type = NavType.StringType
                }
            )
        ) {
            val itemSpotJson = it.arguments?.getString(ScreenState.KEY_ITEM_IMAGE) ?: ""
            val itemSpot = Json.decodeFromString<ItemSpot>(itemSpotJson)
            detailsImageContent(itemSpot)
        }
        bottomSheet(
            route = ScreenState.BottomSheet.Add.route,
            arguments = listOf(
                navArgument(
                    name = ScreenState.KEY_LAT_LNG_MARKER
                ) {
                    type = NavType.StringType
                }
            )
        ) {
            val newMarkerJson =
                it.arguments?.getString(ScreenState.KEY_LAT_LNG_MARKER) ?: ""
            val newMarker = Json.decodeFromString<ItemSpot>(newMarkerJson)
            addItemBottSheetContent(newMarker)
        }

        bottomSheet(
            route = ScreenState.BottomSheet.Edit.route,
            arguments = listOf(
                navArgument(
                    name = ScreenState.KEY_ITEM_SPOT
                ) {
                    type = NavType.StringType
                }
            )
        ) {
            val itemSpotJson =
                it.arguments?.getString(ScreenState.KEY_ITEM_SPOT) ?: ""
            val itemSpot = Json.decodeFromString<ItemSpot>(itemSpotJson)
            editItemBottSheetContent(itemSpot)
        }
    }
}