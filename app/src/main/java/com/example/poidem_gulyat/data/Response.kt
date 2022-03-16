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
}

sealed class ResponseDataBase {
    data class Success<out T>(val value: T) : ResponseDataBase()
    data class Failure(
        val errorBody: Throwable,
    ) : ResponseDataBase()

    object Empty : ResponseDataBase()
    object Looding : ResponseDataBase()
//    companion object {
//        inline fun <T> on(f: () -> T): ResponseDataBase<T> = try {
//            Success(f())
//        } catch (ex: Exception) {
//            Failure(ex)
//        }
//
//        inline fun <T, R, D> ResponseDataBase<T>.zip(
//            success: (T) -> R,
//            errorr: (Throwable) -> R,
//            empty: () -> R,
//        ): R =
//            when (this) {
//                is ResponseDataBase.Success -> success(this.value)
//                is ResponseDataBase.Failure -> errorr(this.errorBody as Exception)
//                is ResponseDataBase.Empty -> empty()
//            }
//    }
}