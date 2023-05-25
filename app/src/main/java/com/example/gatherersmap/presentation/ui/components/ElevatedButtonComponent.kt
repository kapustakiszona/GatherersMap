package com.example.gatherersmap.presentation.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ElevatedButtonComponent(
    onClick: () -> Unit,
    iconVector: ImageVector,
    text: String
) {
    ElevatedButton(
        onClick = {
            onClick()
        },
        contentPadding = PaddingValues(start = 14.dp, end = 14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        )
    ) {
        Icon(
            imageVector = iconVector,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif
        )
    }
}