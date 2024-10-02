package com.haroof.mviplayground.presentation.settings

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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewState: LiveData<SettingsMvi.State>,
    submitIntent: (SettingsMvi.Intent) -> Unit,
) {
    viewState.observeAsState().value?.let { state ->
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
}
//
//@Preview(showBackground = true)
//@Composable
//private fun Preview() {
//    MVIPlaygroundTheme {
//        SettingsScreen(
//            state = SettingsMvi.State(
//                username = "ashoukat",
//                feedbackEnabled = true,
//            ),
//            submitIntent = {},
//        )
//    }
//}
