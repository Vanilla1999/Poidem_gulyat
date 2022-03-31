package com.example.poidem_gulyat.data

import okhttp3.ResponseBody

sealed class Response<out T> {
    data class Success<out T>(val value: T) : Response<T>()
    data class Failure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?,
    ) : Response<Nothing>()

    object Loading : Response<Nothing>()
    object Empty : Response<Nothing>()
}

sealed class ResponseSplash<out T> {
    data class Success<out T>(val value: T) : ResponseSplash<T>()
    object Failure : ResponseSplash<Nothing>()
    object Loading : ResponseSplash<Nothing>()
}

sealed class ResponseHome {
    object Attraction : ResponseHome()
    object PhotoZone : ResponseHome()
    object Loading : ResponseHome()
    object UserPoint : ResponseHome()
    object Non : ResponseHome()
}

sealed class ResponseDataBase<out T> {
    data class Success<out T>(val value: List<T>) : ResponseDataBase<T>()
    data class SuccessNotList<out T>(val value: T) : ResponseDataBase<T>()
    data class Failure(
        val errorBody: Throwable,
    ) : ResponseDataBase<Nothing>()

    object Empty : ResponseDataBase<Nothing>()
}

sealed class DataToMain<out T> {
    data class Success<out T>(val value: Map<Int,List<T>>) : DataToMain<T>()
    data class Failure(
        val errorBody: Throwable,
    ) : DataToMain<Nothing>()

    object Empty : DataToMain<Nothing>()
    object Clear : DataToMain<Nothing>()
    object Non : DataToMain<Nothing>()
}

sealed class ErrorApp<out T> {
    data class FailureDataBase<out T>(val value: T) : ErrorApp<T>()
    data class FailureUnknown<out T>(val value: T) : ErrorApp<T>()
}
