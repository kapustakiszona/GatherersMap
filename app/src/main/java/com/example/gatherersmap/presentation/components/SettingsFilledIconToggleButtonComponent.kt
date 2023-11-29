package com.example.gatherersmap.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gatherersmap.presentation.main.ui.settings.SettingsDialogContent

@Composable
fun SettingsFilledIconToggleButtonComponent(
) {
    var checked by remember { mutableStateOf(false) }
    FilledIconToggleButton(
        modifier = Modifier.padding(start = 10.dp, top = 10.dp),
        checked = checked,
        onCheckedChange = { checked = it }
    ) {
        if (checked) {
            SettingsDialogContent(
                onCancelClick = { checked = false })
            Icon(
                Icons.Filled.Settings,
                contentDescription = null,

                )
        } else {
            Icon(
                Icons.Outlined.Settings,
                contentDescription = null,
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ButPrev() {
    val checked by remember { mutableStateOf(false) }
    SmallFloatingActionButton(
        onClick = { !checked },
        Modifier.wrapContentSize(),
        shape = CircleShape,
        elevation = FloatingActionButtonDefaults.elevation(5.dp)
    ) {
        if (checked) {
            Icon(
                Icons.Filled.Settings,
                contentDescription = null,
            )
        } else {
            Icon(
                Icons.Outlined.Settings,
                contentDescription = null,
            )
        }
    }
}