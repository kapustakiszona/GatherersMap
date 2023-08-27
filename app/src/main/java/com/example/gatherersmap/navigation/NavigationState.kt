package com.example.gatherersmap.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.gatherersmap.domain.model.ItemSpot
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NavigationState(
    val navHostController: NavHostController,
) {
    fun navigateToDetails(itemSpot: ItemSpot) {
        navHostController.navigate(
            ScreenState.BottSheetHome.Details.getRouteWithArgs(
                itemSpot
            )
        ) {
            popUpTo(navHostController.graph.startDestinationId) {
                saveState = false

            }
            launchSingleTop = true
        }
    }

    fun navigateToAddItem(latLng: LatLng) {
        navHostController.navigate(ScreenState.BottSheetHome.Add.getRouteWithArgs(latLng)) {
            popUpTo(navHostController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
        }
    }

    fun navigateToEditItem(itemSpot: ItemSpot) {
        navHostController.navigate(
            ScreenState.BottSheetHome.Edit.getRouteWithArgs(
                itemSpot
            )
        ) {
            popUpTo(navHostController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
        }
    }

    fun navigateToMap() {
        if (navHostController.currentBackStack.value.isNotEmpty()) {
            navHostController.popBackStack(
                route = ScreenState.BottSheetHome.route,
                inclusive = false
            )
        }
    }
}

@Composable
fun rememberNavigationState(
    navHostController: NavHostController = rememberNavController(),
): NavigationState {
    return remember {
        NavigationState(navHostController)
    }
}