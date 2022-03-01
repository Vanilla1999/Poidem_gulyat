package com.otus.securehomework.data.repository

import com.otus.securehomework.data.dto.TokenResponse
import com.example.poidem_gulyat.data.source.local.UserPreferences
import com.otus.securehomework.data.source.network.TokenRefreshApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import com.example.poidem_gulyat.data.Response as DataResponse

class TokenAuthenticator @Inject constructor(
    private val tokenApi: TokenRefreshApi,
    private val preferences: UserPreferences
) : Authenticator, BaseRepository(tokenApi) {

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            when (val tokenResponse = getUpdatedToken()) {
                is DataResponse.Success -> {
                    preferences.saveAccessTokens(
                        tokenResponse.value.access_token,
                        tokenResponse.value.refresh_token
                    )
                    response.request.newBuilder()
                        .header("Authorization", "Bearer ${tokenResponse.value.access_token}")
                        .build()
                }
                else -> null
            }
        }
    }

    private suspend fun getUpdatedToken(): DataResponse<TokenResponse> {
        val refreshToken = preferences.refreshToken.first()
        return safeApiCall {
            tokenApi.refreshAccessToken(refreshToken)
        }
    }
}