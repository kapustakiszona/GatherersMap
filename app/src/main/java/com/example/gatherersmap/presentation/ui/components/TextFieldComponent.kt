package com.example.gatherersmap.presentation.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.example.gatherersmap.domain.model.ItemSpot

@Composable
fun TextFieldComponent(
    newValue: MutableState<String>,
    modifiedItem: ItemSpot,
    focusManager: FocusManager,
    label: String
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = newValue.value,
        onValueChange = { text ->
            newValue.value = text
            modifiedItem.name = newValue.value
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            capitalization = KeyboardCapitalization.Sentences
        ),
        keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus()
        }),
        label = {
            Text(text = label)
        },
        shape = RoundedCornerShape(15.dp)
    )
}