package com.dhruv.angular_launcher.permission_handler

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionHandler(
    permissions: List<PermissionModel>,
    askPermission: Boolean,
    result: (Map<String, Boolean>) -> Unit = {}
) {
    val activity = LocalContext.current as Activity
    val viewModel: PermissionViewModel = viewModel(
        factory = PermissionViewModelFactory(
            permissions = permissions,
            askPermission = askPermission
        )
    )
    val state = viewModel.state.collectAsState().value

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {
            result(it)
            viewModel.onResult(it)
        }
    )
    LaunchedEffect(key1 = state.askPermission, block = {
        if (state.askPermission) {
            permissionLauncher.launch(state.permissions.toTypedArray())
        }
    })
    LaunchedEffect(key1 = state.navigateToSetting, block = {
        if (state.navigateToSetting) {
            activity.openAppSetting()
            viewModel.onPermissionRequested()
        }
    })


    AnimatedVisibility(
        visible = state.showRational,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        AlertDialog(
            onDismissRequest = {},
            Modifier
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Access denied",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                state.rationals.forEachIndexed { index, item ->
                    Text(
                        text = "${index + 1}) $item",
                        modifier = Modifier.padding(vertical = 8.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                Button(
                    onClick = viewModel::onGrantPermissionClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                ) {
                    Text(text = "Grant Permission", modifier = Modifier.padding(vertical = 4.dp))
                }

            }
        }
    }
}