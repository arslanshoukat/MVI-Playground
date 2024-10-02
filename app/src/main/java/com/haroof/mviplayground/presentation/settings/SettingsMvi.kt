package com.haroof.mviplayground.presentation.settings

import android.os.Build.VERSION
import com.haroof.mviplayground.base.BaseChange
import com.haroof.mviplayground.base.BaseIntent
import com.haroof.mviplayground.base.BaseState

interface SettingsMvi {

    data class State(
        val loading: Boolean = false,
        val username: String = "",
        val feedbackEnabled: Boolean = false,
        val version: String = VERSION.RELEASE,
    ) : BaseState

    sealed interface Intent : BaseIntent {
        data object OnBackPressed : Intent
        data object LogOut : Intent
        data class OnOptionClicked(val isFeedback: Boolean) : Intent
    }

    sealed interface Change : BaseChange {
        data object NoChange : Change
        data class Username(val username: String) : Change
        data class FeedbackEnabled(val enabled: Boolean) : Change
    }
}
