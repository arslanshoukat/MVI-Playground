package com.haroof.mviplayground

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.haroof.mviplayground.presentation.settings.SettingsMvi
import com.haroof.mviplayground.presentation.settings.SettingsScreen
import com.haroof.mviplayground.presentation.settings.SettingsViewModel
import com.haroof.mviplayground.theme.MVIPlaygroundTheme
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<SettingsViewModel>()

    private val liveData = MutableLiveData<SettingsMvi.State>(SettingsMvi.State())
    private val intents = MutableSharedFlow<SettingsMvi.Intent>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.render(intents)
                    .onEach { Log.d("Logger", "Received state: $it") }
                    .collect { liveData.value = it }
            }
        }

        setContent {
            MVIPlaygroundTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    SettingsScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewState = liveData,
                        submitIntent = { intents.tryEmit(it) }
                    )
                }
            }
        }
    }
}
