package com.example.gatherersmap.presentation.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.gatherersmap.domain.model.ItemSpot

@Composable
fun DetailsSheetContent(itemSpot: ItemSpot) {
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = itemSpot.name + itemSpot.id)
        Text(text = itemSpot.description)
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(id = itemSpot.image),
            contentDescription = null
        )
    }
}