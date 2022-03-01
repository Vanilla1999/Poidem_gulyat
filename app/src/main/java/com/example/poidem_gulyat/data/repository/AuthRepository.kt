package com.example.poidem_gulyat.data.repository

import com.example.poidem_gulyat.data.Response
import com.example.poidem_gulyat.data.dto.LoginResponse
import com.otus.securehomework.data.repository.BaseRepository
import com.example.poidem_gulyat.data.source.local.UserPreferences
import com.otus.securehomework.data.source.network.AuthApi
import javax.inject.Inject

class AuthRepository
@Inject constructor(
    private val api: AuthApi,
    private val preferences: UserPreferences
) : BaseRepository(api) {

    suspend fun login(
        email: String,
        password: String
    ): Response<LoginResponse> {
        return safeApiCall { api.login(email, password) }
    }

    suspend fun saveAccessTokens(accessToken: String, refreshToken: String) {
        preferences.saveAccessTokens(accessToken, refreshToken)
    }
}