package com.example.gatherersmap.presentation.main.ui

import android.util.Log
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.gatherersmap.navigation.NavigationState
import com.example.gatherersmap.navigation.ScreenState
import com.example.gatherersmap.presentation.components.CircularProgressBarComponent
import com.example.gatherersmap.presentation.components.reusables.ANIMATION_TIME_HALF_SEC
import com.example.gatherersmap.presentation.components.reusables.ANIMATION_TIME_QTR_SEC
import com.example.gatherersmap.presentation.components.reusables.AnimatedScaleInTransitionForFab
import com.example.gatherersmap.presentation.location.locationService
import com.example.gatherersmap.presentation.main.ui.MainActivity.Companion.TAG
import com.google.android.gms.maps.model.LatLng

@Composable
fun PickLocationFab(
    modifier: Modifier = Modifier,
    navigationState: NavigationState,
    loadingState: Boolean,
    addNewItemSpot: (LatLng) -> Unit,
) {
    val context = LocalContext.current
    val navBackStackEntry = navigationState.navHostController.currentBackStackEntryAsState()
    val visibility = (navBackStackEntry.value?.destination?.route == ScreenState.GoogleMap.route)
    AnimatedScaleInTransitionForFab(
        visible = visibility,
        animateDurationExit = ANIMATION_TIME_QTR_SEC,
        animateDurationEnter = ANIMATION_TIME_HALF_SEC
    ) {
        if (loadingState) {
            Log.d(TAG, "PickLocationFab: ProgressBar start")
            CircularProgressBarComponent(true)
        } else {
            ExtendedFloatingActionButton(
                modifier = modifier,
                text = { Text(text = "New mushroom") },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                shape = RoundedCornerShape(15.dp),
                onClick = {
                    locationService(context) { currentLocation ->
                        addNewItemSpot(
                            LatLng(
                                currentLocation.latitude,
                                currentLocation.longitude
                            )
                        )
                    }
                },
            )
        }
    }
}