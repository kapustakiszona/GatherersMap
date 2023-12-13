package com.example.gatherersmap.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.gatherersmap.domain.model.ItemSpot
import com.google.android.gms.maps.model.LatLng

class NavigationState(
    val navHostController: NavHostController,
) {
    fun navigateToDetails(itemSpot: ItemSpot) {
        navHostController.navigate(
            ScreenState.BottomSheet.Details.getRouteWithArgs(
                itemSpot
            )
        ) {
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToAddItem(latLng: LatLng) {
        navHostController.navigate(ScreenState.BottomSheet.Add.getRouteWithArgs(latLng)) {
            popUpTo(ScreenState.GoogleMap.route)
        }
    }

    fun navigateToEditItem(itemSpot: ItemSpot) {
        navHostController.navigate(
            ScreenState.BottomSheet.Edit.getRouteWithArgs(
                itemSpot = itemSpot
            )
        ) {
            popUpTo(route = ScreenState.BottomSheet.Details.getRouteWithArgs(itemSpot)) {
                saveState = true
            }
            restoreState = true
            launchSingleTop = true
        }
    }

    fun navigateToMap() {
        navHostController.popBackStack(
            route = ScreenState.GoogleMap.route,
            inclusive = false
        )
    }

    fun navigateToDetailsImage(itemSpot: ItemSpot) {
        navHostController.navigate(
            ScreenState.BottomSheet.Image.getRouteWithArgs(itemSpot)
        ) {
            popUpTo(navHostController.graph.startDestinationId) {
                saveState = true
                inclusive = true
            }
        }
    }

    fun navigateBackFromImage(itemSpot: ItemSpot) {
        navHostController.navigate(
            ScreenState.GoogleMap.route
        ) {
            popUpTo(ScreenState.BottomSheet.Details.getRouteWithArgs(itemSpot))
            launchSingleTop = true
            restoreState = true
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