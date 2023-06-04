package com.example.gatherersmap.presentation.main.ui.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun EnterBotSheetContent() {

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Выберите Вашу точку, чтобы увидеть детали, либо добавте новую")
        Button(
            onClick = { }
        ) {
            Text(text = "Добавить точку")
        }
    }
}