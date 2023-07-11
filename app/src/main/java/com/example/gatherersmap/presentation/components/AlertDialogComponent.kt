package com.example.gatherersmap.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

@Composable
fun AlertDialogComponent(
    title: String,
    description: String,
    onClick: (Boolean) -> Unit,
    textButton: String
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                    fontWeight = FontWeight.Bold
                )
            )
        },
        text = {
            Text(text = description)
        },
        confirmButton = {
            Button(
                onClick = {
                    onClick(true)
                }
            ) {
                Text(text = textButton)
            }
        }
    )
}