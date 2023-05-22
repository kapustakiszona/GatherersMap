package com.example.gatherersmap.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DeletingDialogComposable(
    onDeleteItem: () -> Unit,
    onClose: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onDismissRequest.
            onClose()
        },
        title = {
            Text(text = "Delete")
        },
        text = {
            Text(text = "Delete the marker?")
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDeleteItem()
                    onClose()
                }
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onClose()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}
