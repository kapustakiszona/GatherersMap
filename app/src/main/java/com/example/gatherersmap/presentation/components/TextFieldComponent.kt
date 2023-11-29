package com.example.gatherersmap.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun TexFieldPrev() {
    TextFieldComponent(
        currentValue = "String 1 2 3",
        modifiedValue = {},
        label = "Test 1 2 3 String"
    )
}

@Composable
fun TextFieldComponent(
    currentValue: String,
    modifiedValue: (String) -> Unit,
    label: String,
) {
    var newValue by remember {
        mutableStateOf(currentValue)
    }
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = newValue,
        onValueChange = { text ->
            newValue = text
            modifiedValue(newValue)
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            capitalization = KeyboardCapitalization.Sentences
        ),
        keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus()
        }),
        label = {
            Text(text = label, fontFamily = FontFamily.Monospace)
        },
        shape = RoundedCornerShape(15.dp)
    )
}