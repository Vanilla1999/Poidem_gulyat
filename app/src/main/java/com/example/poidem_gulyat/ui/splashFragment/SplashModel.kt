package com.example.poidem_gulyat.ui.splashFragment

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.otus.securehomework.data.Response
import com.otus.securehomework.data.ResponseSplash
import com.otus.securehomework.data.source.local.UserPreferences
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import javax.inject.Inject
import javax.inject.Named

class SplashModel (
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _prefStateFlow: MutableStateFlow<ResponseSplash<Any?>> = MutableStateFlow(ResponseSplash.Loading)
    val prefStateFlow: Flow<ResponseSplash<Any?>> = _prefStateFlow

    fun checkUser() {
        viewModelScope.launch {
            userPreferences.accessToken.collect {
                if(it==null){
                    Log.d("SplashModel", "null")
                    _prefStateFlow.value = ResponseSplash.Failure
                } else{
                    Log.d("SplashModel", it)
                    _prefStateFlow.value = ResponseSplash.Success(it)
                }
            }
        }
    }

    class FactorySplash @Inject constructor(
        private val userPreferences: UserPreferences
    ):ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SplashModel(userPreferences) as T
        }
    }
}
