package com.example.gatherersmap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.gatherersmap.presentation.ui.BottomSheet
import com.example.gatherersmap.presentation.vm.MapViewModel
import com.example.gatherersmap.ui.theme.GatherersMapTheme
import com.google.android.gms.location.FusedLocationProviderClient

class MainActivity : ComponentActivity() {


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val viewModel: MapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GatherersMapTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BottomSheet()
                }
            }
        }
    }
}

