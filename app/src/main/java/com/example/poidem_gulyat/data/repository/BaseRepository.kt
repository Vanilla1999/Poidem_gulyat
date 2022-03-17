package com.otus.securehomework.data.repository

import com.example.poidem_gulyat.data.ResponseDataBase
import com.otus.securehomework.data.source.SafeApiCall
import com.otus.securehomework.data.source.network.BaseApi
import kotlinx.coroutines.flow.FlowCollector
import java.lang.Exception

abstract class BaseRepository(
    private val api: BaseApi,
) : SafeApiCall {

    suspend fun logout() = safeApiCall {
        api.logout()
    }
}

abstract class BaseRepositoryDataBase(
) {
    protected suspend fun <T> doWork(value: List<T>, collector: FlowCollector<ResponseDataBase<*>>) {
        try {
            if (value.isEmpty()) {
                collector.emit(ResponseDataBase.Empty)
            } else {
                collector.emit(ResponseDataBase.Success(value))
            }
        } catch (e: Exception) {
            collector.emit(ResponseDataBase.Failure(e))
        }
    }
}