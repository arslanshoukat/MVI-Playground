package com.haroof.mviplayground.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.MutableStateFlow

abstract class BaseViewModel<State : BaseState, Intent : BaseIntent>(initialSate: State) : ViewModel() {

    protected val _state = MutableStateFlow<State>(initialSate)
    val state = _state

    protected val intents = Channel<Intent>(capacity = UNLIMITED)

    fun dispatch(intent: Intent) {
        intents.trySend(intent)
    }
}
