package com.example.poidem_gulyat.data.repository.markers

import com.example.poidem_gulyat.data.dto.UserLocation
import com.example.poidem_gulyat.data.dto.UserPoint
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

interface UserPointRepository {

    suspend fun getAllUserPointsSuitRating(rating:Double): Flow<List<UserPoint>>

    suspend fun getAllUserPoints(): Flow<List<UserPoint>>

    suspend fun insert(item: UserPoint)

    suspend fun delete(list: List<UserPoint>)
}

interface MarkerRepository {

    suspend fun <T> getAllTSuitRating(rating:Double): Flow<List<T>>

    suspend fun <T> getAllT(): Flow<List<T>>

    suspend fun <T> insert(item: T)

    suspend fun <T> delete(list: List<T>)
}