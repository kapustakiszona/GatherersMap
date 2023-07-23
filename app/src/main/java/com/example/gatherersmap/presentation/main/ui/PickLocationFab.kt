package com.example.gatherersmap.presentation.main.ui

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gatherersmap.navigation.BottomSheetScreenState
import com.example.gatherersmap.presentation.components.reusables.ANIMATION_TIME_HALF_SEC
import com.example.gatherersmap.presentation.components.reusables.AnimatedScaleInTransition
import com.example.gatherersmap.presentation.location.locationService
import com.example.gatherersmap.presentation.main.ui.map.MapEvent
import com.example.gatherersmap.presentation.main.vm.MapViewModel
import com.google.android.gms.maps.model.LatLng

@Composable
fun PickLocationFab(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = viewModel(),
    currentSheetState: BottomSheetScreenState,
) {
    val context = LocalContext.current
    AnimatedScaleInTransition(
        visible = (currentSheetState == BottomSheetScreenState.Initial),
        animateDuration = ANIMATION_TIME_HALF_SEC
    ) {
        ExtendedFloatingActionButton(
            modifier = modifier,
            text = { Text(text = "New mushroom") },
            icon = { Icon(Icons.Default.Add, contentDescription = null) },
            shape = RoundedCornerShape(15.dp),
            onClick = {
                locationService(context) { currentLocation ->
                    viewModel.onEvent(
                        MapEvent.OnAddItemClick(
                            LatLng(
                                currentLocation.latitude,
                                currentLocation.longitude
                            )
                        )
                    )
                }
            },
        )
    }
}