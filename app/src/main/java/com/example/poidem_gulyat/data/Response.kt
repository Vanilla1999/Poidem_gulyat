package com.example.poidem_gulyat.data

import okhttp3.ResponseBody

sealed class Response<out T> {
    data class Success<out T>(val value: T) : Response<T>()
    data class Failure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?
    ) : Response<Nothing>()
    object Loading : Response<Nothing>()
    object Empty : Response<Nothing>()
}
sealed class ResponseSplash<out T> {
    data class Success<out T>(val value: T) : ResponseSplash<T>()
    object Failure : ResponseSplash<Nothing>()
    object Loading : ResponseSplash<Nothing>()
}