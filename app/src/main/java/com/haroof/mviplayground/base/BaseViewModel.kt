package com.haroof.mviplayground.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.MutableStateFlow

abstract class BaseViewModel<State : BaseState, Intent : BaseIntent> : ViewModel()
