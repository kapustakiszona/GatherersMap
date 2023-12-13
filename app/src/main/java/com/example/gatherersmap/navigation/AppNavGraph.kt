package com.example.gatherersmap.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gatherersmap.domain.model.ItemSpot

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    mapScreenContent: @Composable () -> Unit,
    addItemBottSheetContent: @Composable (ItemSpot) -> Unit,
    detailsItemBottSheetContent: @Composable (ItemSpot) -> Unit,
    editItemBottSheetContent: @Composable (ItemSpot) -> Unit,
    detailsImageContent: @Composable (ItemSpot) -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = ScreenState.GoogleMap.route,
    ) {

        composable(route = ScreenState.GoogleMap.route) {
            mapScreenContent()
        }
        bottomSheetNavGraph(
            detailsItemBottSheetContent = detailsItemBottSheetContent,
            detailsImageContent = detailsImageContent,
            addItemBottSheetContent = addItemBottSheetContent,
            editItemBottSheetContent = editItemBottSheetContent
        )
    }
}