package com.otus.securehomework.data.source.network

import com.example.poidem_gulyat.data.dto.LoginResponse
import retrofit2.http.GET

interface UserApi : BaseApi{

    @GET("user")
    suspend fun getUser(): LoginResponse
}