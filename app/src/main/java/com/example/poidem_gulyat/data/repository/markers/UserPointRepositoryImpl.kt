package com.example.poidem_gulyat.data.repository.markers

import com.example.poidem_gulyat.data.ResponseDataBase
import com.example.poidem_gulyat.data.dto.UserLocation
import com.example.poidem_gulyat.data.dto.UserPoint
import com.example.poidem_gulyat.data.source.database.DatabaseMain
import com.otus.securehomework.data.repository.BaseRepositoryDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class UserPointRepositoryImpl @Inject constructor(
    private val databaseSource: DatabaseMain
): UserPointRepository, BaseRepositoryDataBase() {
    override suspend fun getAllUserPointsSuitRating(rating: Double): Flow<ResponseDataBase<Any?>> {
        return databaseSource.userPoint().getAllUserPointsSuitRating(rating).transform {
            doWork(it, this)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getAllUserPoints(): Flow<ResponseDataBase<Any?>> {
        return databaseSource.userPoint().getAllUserPoints().transform {
            doWork(it, this)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun insert(item: UserPoint) {
        databaseSource.userPoint().insertOrUpdate(item)
    }

    override suspend fun insertList(item: List<UserPoint>) {
        databaseSource.userPoint().insertOrUpdate(item)
    }

    override suspend fun delete(list: List<UserPoint>) {
        databaseSource.userPoint().delete(list)
    }
}

