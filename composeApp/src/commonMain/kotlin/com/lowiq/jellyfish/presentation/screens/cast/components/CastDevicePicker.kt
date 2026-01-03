package com.lowiq.jellyfish.presentation.screens.cast.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.CastConnected
import androidx.compose.material.icons.filled.Speaker
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lowiq.jellyfish.domain.cast.CastDevice
import com.lowiq.jellyfish.domain.cast.CastDeviceType

@Composable
fun CastDevicePicker(
    devices: List<CastDevice>,
    connectedDevice: CastDevice?,
    onDeviceSelected: (CastDevice) -> Unit,
    onDisconnect: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (connectedDevice != null) "Connected to ${connectedDevice.name}" else "Select a device"
            )
        },
        text = {
            if (devices.isEmpty() && connectedDevice == null) {
                Text("No devices found. Make sure your Chromecast is on the same network.")
            } else {
                LazyColumn {
                    if (connectedDevice != null) {
                        item {
                            DeviceItem(
                                device = connectedDevice,
                                isConnected = true,
                                onClick = { onDisconnect() }
                            )
                        }
                    }
                    items(devices.filter { it.id != connectedDevice?.id }) { device ->
                        DeviceItem(
                            device = device,
                            isConnected = false,
                            onClick = { onDeviceSelected(device) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },
        dismissButton = if (connectedDevice != null) {
            {
                TextButton(onClick = onDisconnect) {
                    Text("Disconnect")
                }
            }
        } else null
    )
}

@Composable
private fun DeviceItem(
    device: CastDevice,
    isConnected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val icon = when {
            isConnected -> Icons.Filled.CastConnected
            device.type == CastDeviceType.CHROMECAST -> Icons.Filled.Tv
            device.type == CastDeviceType.AIRPLAY -> Icons.Filled.Speaker
            else -> Icons.Filled.Cast
        }

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = if (isConnected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = device.name,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isConnected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
            if (isConnected) {
                Text(
                    text = "Tap to disconnect",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
