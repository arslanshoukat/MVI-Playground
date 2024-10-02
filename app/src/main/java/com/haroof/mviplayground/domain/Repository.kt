package com.haroof.mviplayground.domain

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object Repository {

    fun isFeedbackFeatureEnabled(): Flow<Boolean> {
        return flow {
            delay(1000)
            emit(true)
        }
    }
}
