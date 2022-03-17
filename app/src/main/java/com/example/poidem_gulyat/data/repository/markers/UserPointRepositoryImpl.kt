package com.example.poidem_gulyat.data.repository.markers

import com.example.poidem_gulyat.data.dto.UserLocation
import com.example.poidem_gulyat.data.dto.UserPoint
import com.example.poidem_gulyat.data.source.database.DatabaseMain
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPointRepositoryImpl @Inject constructor(
    private val databaseSource: DatabaseMain
): UserPointRepository {
    override suspend fun getAllUserPointsSuitRating(rating: Double): Flow<List<UserPoint>> {
        return databaseSource.userPoint().getAllUserPointsSuitRating(rating)
    }

    override suspend fun getAllUserPoints(): Flow<List<UserPoint>> {
        return databaseSource.userPoint().getAllUserPoints()
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
