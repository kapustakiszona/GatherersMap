package com.example.gatherersmap.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.presentation.main.ui.MainActivity.Companion.TAG
import com.google.android.gms.maps.model.LatLng

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
                saveState = true
            }
            launchSingleTop = true
        }
    }

    fun navigateToAddItem(latLng: LatLng) {
        navHostController.navigate(ScreenState.BottSheetHome.Add.getRouteWithArgs(latLng)) {
//            popUpTo(navHostController.graph.startDestinationId) {
//                saveState = true
//            }
//            launchSingleTop = true
        }
    }

    fun navigateToEditItem(itemSpot: ItemSpot) { //navigateUp
        navHostController.navigate(
            ScreenState.BottSheetHome.Edit.getRouteWithArgs(
                itemSpot = itemSpot
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
                route = ScreenState.GoogleMap.route,
                inclusive = false
            )
        }
    }
}

@Composable
fun NavigationHandler(
    navigationDestination: NavigationDestinations,
    navigationState: NavigationState,
) {
    when (navigationDestination) {
        is NavigationDestinations.Add -> {
            Log.d(TAG, "NavigationHandler: navigate to add")
            navigationState.navigateToAddItem(navigationDestination.latLng)
        }

        is NavigationDestinations.Details -> {
            Log.d(TAG, "NavigationHandler: navigate to details")
            navigationState.navigateToDetails(navigationDestination.itemSpot)
        }

        is NavigationDestinations.Edit -> {
            Log.d(TAG, "NavigationHandler: navigate to edit")
            navigationState.navigateToEditItem(navigationDestination.itemSpot)
        }

        is NavigationDestinations.Map -> {
            Log.d(TAG, "NavigationHandler: navigate to map")
            navigationState.navigateToMap()
        }

        is NavigationDestinations.Current -> {}
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