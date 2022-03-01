package com.example.poidem_gulyat.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.poidem_gulyat.ui.splashFragment.SplashModel
import com.example.poidem_gulyat.data.Response
import com.example.poidem_gulyat.data.dto.LoginResponse
import com.example.poidem_gulyat.data.repository.AuthRepository
import com.example.poidem_gulyat.data.source.local.UserPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel(
    private val userPreferences: UserPreferences,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginStateFlow: MutableStateFlow<Response<LoginResponse?>> = MutableStateFlow(
        Response.Empty)
    val loginStateFlow: StateFlow<Response<LoginResponse?>> = _loginStateFlow.asStateFlow()

    fun login(
        email: String,
        password: String
    ) = viewModelScope.launch {
        _loginStateFlow.value = Response.Loading
        _loginStateFlow.value = authRepository.login(email, password)
    }

    suspend fun saveAccessTokens(accessToken: String, refreshToken: String) {
        userPreferences.saveAccessTokens(accessToken, refreshToken)
    }
}

class FactoryLogin @Inject constructor(
    private val userPreferences: UserPreferences,
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(userPreferences,authRepository) as T
    }
}