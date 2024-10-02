package com.haroof.mviplayground.domain

import android.util.Log
import com.haroof.mviplayground.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object AuthManager {

    fun getUser(): Flow<User> {
        return flow {
            for(i in 1..600){
                emit(User("ashoukat$i"))
                delay(5_000)
            }
        }
    }

    suspend fun logout(){
        Log.d("AuthManager", "logout()")
    }
}
