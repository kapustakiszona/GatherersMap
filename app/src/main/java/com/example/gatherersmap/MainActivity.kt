package com.example.gatherersmap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gatherersmap.data.ItemSpotDatabase
import com.example.gatherersmap.data.ItemSpotRepositoryImpl
import com.example.gatherersmap.presentation.ui.BottomSheet
import com.example.gatherersmap.presentation.vm.MapViewModel
import com.example.gatherersmap.presentation.vm.MapViewModelFactory
import com.example.gatherersmap.ui.theme.GatherersMapTheme

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GatherersMapTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: MapViewModel =
                        viewModel(
                            factory = MapViewModelFactory(
                                ItemSpotRepositoryImpl(
                                    ItemSpotDatabase
                                )
                            )
                        )
                    BottomSheet(viewModel = viewModel)
                }
            }
        }
    }
}

