package com.haroof.mviplayground.presentation.settings

import android.util.Log
import com.haroof.mviplayground.base.BaseViewModel
import com.haroof.mviplayground.domain.AuthManager
import com.haroof.mviplayground.domain.Navigator
import com.haroof.mviplayground.domain.Repository
import com.haroof.mviplayground.presentation.settings.SettingsMvi.Change
import com.haroof.mviplayground.presentation.settings.SettingsMvi.Intent
import com.haroof.mviplayground.presentation.settings.SettingsMvi.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.scan

class SettingsViewModel : BaseViewModel<State, Intent>() {

    private val repository = Repository
    private val authManager = AuthManager
    private val navigator = Navigator

    fun render(intents: MutableSharedFlow<Intent>): Flow<State> {
        val userNameChange = authManager.getUser().map {
            Change.Username(it.username)
        }
        val feedbackEnabledFlow = repository.isFeedbackFeatureEnabled().map {
            Change.FeedbackEnabled(it)
        }
        val mappedIntents = intents.map { intent ->
            Log.d("Logger", "Received intent: $intent")

            when (intent) {
                Intent.LogOut -> authManager.logout()
                Intent.OnBackPressed -> navigator.popBackStack()
                is Intent.OnOptionClicked -> navigator.navigate(if (intent.isFeedback) "Feedback" else "CaptureMode")
            }

            Change.NoChange
        }

        return merge(userNameChange, feedbackEnabledFlow, mappedIntents)
            .scan(State(loading = true)) { state: State, change: Change ->
                when (change) {
                    is Change.FeedbackEnabled -> state.copy(feedbackEnabled = change.enabled)
                    is Change.Username -> state.copy(loading = false, username = change.username)
                    Change.NoChange -> state
                }
            }
            .distinctUntilChanged()
    }
}
