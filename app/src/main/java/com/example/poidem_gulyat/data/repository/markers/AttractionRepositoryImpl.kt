package com.example.poidem_gulyat.data.repository.markers

import com.example.poidem_gulyat.data.ResponseDataBase
import com.example.poidem_gulyat.data.dto.Attraction
import com.example.poidem_gulyat.data.source.database.DatabaseMain
import com.otus.securehomework.data.repository.BaseRepositoryDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.lang.Exception
import javax.inject.Inject

class AttractionRepositoryImpl @Inject constructor(
    private val databaseSource: DatabaseMain,
) : AttractionRepository, BaseRepositoryDataBase() {
    override suspend fun getAllAttractionsSuitRating(rating: Double): Flow<ResponseDataBase<Any?>> {
        return databaseSource.attraction().getAllAttractionSuitRating(rating).transform {
            doWork(it, this)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getAllAttractions(): Flow<ResponseDataBase<Any?>> {
        return databaseSource.attraction().getAllAttractions().transform {
            doWork(it, this)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun insert(item: Attraction) {
        databaseSource.attraction().insertOrUpdate(item)
    }

    override suspend fun insertList(item: List<Attraction>) {
        databaseSource.attraction().insertOrUpdate(item)
    }

    override suspend fun delete(list: List<Attraction>) {
        databaseSource.attraction().delete(list)
    }
}