package com.haroof.mviplayground.domain

import android.util.Log

object Navigator {

    suspend fun popBackStack(){
        Log.d("Navigator", "popBackStack()")
    }

    suspend fun navigate(destination:String){
        Log.d("Navigator", "navigate($destination)")
    }

}
