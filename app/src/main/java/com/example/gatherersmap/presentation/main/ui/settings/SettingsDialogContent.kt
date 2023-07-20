@file:OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)

package com.example.gatherersmap.presentation.main.ui.settings

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gatherersmap.R
import com.example.gatherersmap.presentation.components.reusables.AnimatedEntryExitDialog
import com.example.gatherersmap.presentation.location.locationService
import com.example.gatherersmap.presentation.main.ui.MainActivity.Companion.TAG
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@Preview
@Composable
fun PrevSet() {
    SettingsCurrentPermissionState(
        modifier = Modifier.background(
            color = Color.Red.copy(
                alpha = 0.1f
            )
        )
    )
}

@Composable
fun SettingsDialogContent(
    //  viewModel: MapViewModel = viewModel(),
    onCancelClick: () -> Unit,
) {
    //стейт должен прилетать из вм и зависеть от вкл\выкл gps
    var locationItemEnabledState by remember { mutableStateOf(false) }
    val context = LocalContext.current
    AnimatedEntryExitDialog(
        onDismissRequest = onCancelClick,
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
        ) {
            Column(
                modifier = Modifier.padding(start = 20.dp, top = 10.dp, end = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Settings", style = TextStyle(fontSize = 22.sp))
                Divider(modifier = Modifier.fillMaxWidth().padding(top = 5.dp, bottom = 5.dp))
                val permissionState =
                    rememberMultiplePermissionsState(
                        listOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    )
                if (locationItemEnabledState) {
                    Log.d(TAG, "SettingsDialogContent: switch ON")
                    locationService(context){}
                }
                if (!permissionState.allPermissionsGranted) {
                    SettingsCurrentPermissionState(
                        modifier = Modifier.background(
                            color = Color.Red.copy(
                                alpha = 0.1f
                            )
                        )
                    )
                }
                SettingSwitchItem(
                    modifier = Modifier,
                    checked = locationItemEnabledState,
                    onCheckedChange = {
                        locationItemEnabledState = it
                    },
                    title = R.string.settings_location_title,
                    description = R.string.settings_location_description,
                )
            }
        }
    }
}

@Composable
fun SettingsCurrentPermissionState(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1.0f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Permissions not received",
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
            )
            Text(
                text = "Go to settings to get location permissions",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        val context = LocalContext.current
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts(
                "package",
                context.packageName,
                null
            )
        )
        OutlinedButton(
            onClick = { context.startActivity(intent) },
            contentPadding = ButtonDefaults.ContentPadding,
            shape = RoundedCornerShape(20.dp),
        ) {
            Text(text = "Enable")
        }
    }
}

@Composable
private fun SettingSwitchItem(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    @StringRes title: Int,
    @StringRes description: Int,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Switch,
                onValueChange = onCheckedChange
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1.0f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val contentAlpha = if (enabled) ContentAlpha.high else ContentAlpha.disabled

            Text(
                text = stringResource(id = title),
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                modifier = Modifier.alpha(contentAlpha)
            )
            Text(
                text = stringResource(id = description),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.alpha(contentAlpha)
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = null,
            enabled = enabled
        )
    }
}