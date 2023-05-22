

package com.example.gatherersmap.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.presentation.components.ElevatedButtonComponent

@Composable
fun EditDetailsSheetContent(
    itemSpot: ItemSpot,
    onSaveClicked: (ItemSpot) -> Unit
) {
    val modifiedItem by remember { mutableStateOf(itemSpot) }
    var newName by rememberSaveable { mutableStateOf(itemSpot.name) }
    var newDescription by rememberSaveable { mutableStateOf(itemSpot.description) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.padding(start = 20.dp, top = 10.dp, end = 20.dp)
    ) {


        OutlinedTextField(
            value = newName,
            onValueChange = { text ->
                newName = text
                modifiedItem.name = newName
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Sentences
            ),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }),
            label = {
                Text(text = "Name")
            },
            shape = RoundedCornerShape(15.dp)
        )

        OutlinedTextField(
            value = newDescription,
            onValueChange = { description ->
                newDescription = description
                modifiedItem.description = newDescription
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }),
            label = {
                Text(text = "Description")
            },
            shape = RoundedCornerShape(15.dp)
        )

        ElevatedButtonComponent(
            onClick = {
                onSaveClicked(modifiedItem)
            },
            iconVector = Icons.Outlined.Check,
            text = "Save"
        )
    }

}
