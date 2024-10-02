package com.haroof.mviplayground.presentation.settings

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.repeatOnLifecycle
import com.haroof.mviplayground.theme.MVIPlaygroundTheme
import kotlinx.coroutines.flow.onEach

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val state by produceState(SettingsMvi.State(), viewModel.state, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.state
                .onEach { Log.d("Logger", "State collected: $it") }
                .collect { this@produceState.value = it }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    Log.d("Logger", "onCreate")
                }

                Lifecycle.Event.ON_START -> {
                    Log.d("Logger", "On Start")
                }

                Lifecycle.Event.ON_RESUME -> {
                    Log.d("Logger", "On Resume")
                }

                Lifecycle.Event.ON_PAUSE -> {
                    Log.d("Logger", "On Pause")
                }

                Lifecycle.Event.ON_STOP -> {
                    Log.d("Logger", "On Stop")
                }

                Lifecycle.Event.ON_DESTROY -> {
                    Log.d("Logger", "On Destroy")
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    SettingsScreen(
        modifier = modifier,
        state = state,
        submitIntent = viewModel::dispatch,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    modifier: Modifier = Modifier,
    state: SettingsMvi.State,
    submitIntent: (SettingsMvi.Intent) -> Unit,
) {
    Surface(modifier = modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TopAppBar(
                title = { Text(text = "Settings") },
                navigationIcon = {
                    IconButton(onClick = { submitIntent(SettingsMvi.Intent.OnBackPressed) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
            )

            if (state.loading) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(vertical = 32.dp, horizontal = 24.dp)
                ) {
                    Text(text = "Hello ${state.username}!")

                    ListItem(
                        headlineContent = { Text(text = "Default capture mode") },
                        supportingContent = { Text(text = "Camera") },
                        modifier = Modifier.clickable {
                            submitIntent(SettingsMvi.Intent.OnOptionClicked(false))
                        },
                    )

                    if (state.feedbackEnabled) {
                        TextButton(onClick = { submitIntent(SettingsMvi.Intent.OnOptionClicked(true)) }) {
                            Text(text = "Submit feedback")
                        }
                    }

                    Button(onClick = { submitIntent(SettingsMvi.Intent.LogOut) }) {
                        Text(text = "Logout")
                    }

                    Text(text = "Version: ${state.version}")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    MVIPlaygroundTheme {
        SettingsScreen(
            state = SettingsMvi.State(
                username = "ashoukat",
                feedbackEnabled = true,
            ),
            submitIntent = {},
        )
    }
}
