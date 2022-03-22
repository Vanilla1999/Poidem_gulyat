package com.example.poidem_gulyat.data.repository.markers

import com.example.poidem_gulyat.data.ResponseDataBase

import com.example.poidem_gulyat.data.dto.MarkerPoint
import com.example.poidem_gulyat.data.dto.UserLocation
import com.example.poidem_gulyat.data.dto.UserPoint
import com.example.poidem_gulyat.data.source.database.DatabaseMain
import com.otus.securehomework.data.repository.BaseRepositoryDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class MarkerRepositoryImpl @Inject constructor(
    private val databaseSource: DatabaseMain
): MarkerRepository, BaseRepositoryDataBase() {


    override suspend fun getAllMarkersSuitRating(rating: Double): Flow<ResponseDataBase<MarkerPoint>> {
      return  databaseSource.marker().getAllMarkerSuitRating(rating).transform {
            doWork(it, this)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getAllMarksers(): Flow<ResponseDataBase<MarkerPoint>> {
        return  databaseSource.marker().getAllMarksers().transform {
            doWork(it, this)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getAllMarkersByType(type: Int): Flow<ResponseDataBase<MarkerPoint>> {
        return  databaseSource.marker().getAllMarkersByType(type).transform {
            doWork(it, this)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getAllMarkersByTypeSuitRating(
        type: Int,
        rating: Double,
    ): Flow<ResponseDataBase<MarkerPoint>> {
        return  databaseSource.marker().getAllMarkersByTypeSuitRating(type ,rating).transform {
            doWork(it, this)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun insert(item: MarkerPoint) {
        databaseSource.marker().insertOrUpdate(item)
    }

    override suspend fun insertList(item: List<MarkerPoint>) {
        databaseSource.marker().insertOrUpdate(item)
    }

    override suspend fun delete(list: List<MarkerPoint>) {
        databaseSource.marker().delete(list)
    }
}

