

package com.example.gatherersmap.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gatherersmap.domain.model.ItemSpot
import com.example.gatherersmap.presentation.components.ElevatedButtonComponent
import com.example.gatherersmap.presentation.components.TextFieldComponent

@Composable
fun EditDetailsSheetContent(
    itemSpot: ItemSpot,
    onSaveClicked: (ItemSpot) -> Unit
) {
    val modifiedItem by remember { mutableStateOf(itemSpot) }
    val newName = rememberSaveable { mutableStateOf(itemSpot.name) }
    val newDescription = rememberSaveable { mutableStateOf(itemSpot.description) }


    Column(
        modifier = Modifier.padding(start = 20.dp, top = 10.dp, end = 20.dp)
    ) {

        TextFieldComponent(
            modifiedItem = modifiedItem,
            label = "Name",
            newValue = newName,
        )
        TextFieldComponent(
            modifiedItem = modifiedItem,
            label = "Description",
            newValue = newDescription
        )
//        OutlinedTextField(
//            value = newName,
//            onValueChange = { text ->
//                newName = text
//                modifiedItem.name = newName
//            },
//            keyboardOptions = KeyboardOptions.Default.copy(
//                imeAction = ImeAction.Done,
//                capitalization = KeyboardCapitalization.Sentences
//            ),
//            keyboardActions = KeyboardActions(onDone = {
//                focusManager.clearFocus()
//            }),
//            label = {
//                Text(text = "Name")
//            },
//            shape = RoundedCornerShape(15.dp)
//        )

//        OutlinedTextField(
//            value = newDescription,
//            onValueChange = { description ->
//                newDescription = description
//                modifiedItem.description = newDescription
//            },
//            keyboardOptions = KeyboardOptions(
//                capitalization = KeyboardCapitalization.Sentences,
//                imeAction = ImeAction.Done
//            ),
//            keyboardActions = KeyboardActions(onDone = {
//                focusManager.clearFocus()
//            }),
//            label = {
//                Text(text = "Description")
//            },
//            shape = RoundedCornerShape(15.dp)
//        )

        ElevatedButtonComponent(
            onClick = {
                onSaveClicked(modifiedItem)
            },
            iconVector = Icons.Outlined.Check,
            text = "Save"
        )
    }

}
