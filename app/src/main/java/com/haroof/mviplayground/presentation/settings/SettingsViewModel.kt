package com.haroof.mviplayground.presentation.settings

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.haroof.mviplayground.base.BaseViewModel
import com.haroof.mviplayground.domain.AuthManager
import com.haroof.mviplayground.domain.Navigator
import com.haroof.mviplayground.domain.Repository
import com.haroof.mviplayground.presentation.settings.SettingsMvi.Change
import com.haroof.mviplayground.presentation.settings.SettingsMvi.Intent
import com.haroof.mviplayground.presentation.settings.SettingsMvi.State
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.scan

class SettingsViewModel : BaseViewModel<State, Intent>(State(loading = true)) {

    private val repository = Repository
    private val authManager = AuthManager
    private val navigator = Navigator

    init {
        val userNameChange = authManager.getUser().map {
            Change.Username(it.username)
        }
        val feedbackEnabledFlow = repository.isFeedbackFeatureEnabled().map {
            Change.FeedbackEnabled(it)
        }
        val mappedIntents = intents.receiveAsFlow().map { intent ->
            Log.d("Logger", "Received intent: $intent")

            when (intent) {
                Intent.LogOut -> authManager.logout()
                Intent.OnBackPressed -> navigator.popBackStack()
                is Intent.OnOptionClicked -> navigator.navigate(if (intent.isFeedback) "Feedback" else "CaptureMode")
            }

            Change.NoChange
        }

        listOf(userNameChange, feedbackEnabledFlow, mappedIntents)
            .merge()
            .scan(State(loading = true)) { state: State, change: Change ->
                when (change) {
                    is Change.FeedbackEnabled -> state.copy(feedbackEnabled = change.enabled)
                    is Change.Username -> state.copy(loading = false, username = change.username)
                    Change.NoChange -> state
                }
            }
            .onEach {
                Log.d("Logger", "Received state: $it")
                _state.value = it
            }
            .launchIn(viewModelScope)
    }
}
