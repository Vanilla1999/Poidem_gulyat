package com.example.poidem_gulyat.data.repository.markers

import com.example.poidem_gulyat.data.dto.UserLocation
import com.example.poidem_gulyat.data.dto.UserPoint
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

interface MainRepo <T>{
    suspend fun insert(item: T)

    suspend fun insertList(item:List<T>)

    suspend fun delete(list: List<T>)
}

interface UserPointRepository :MainRepo<UserPoint> {

    suspend fun getAllUserPointsSuitRating(rating:Double): Flow<List<UserPoint>>

    suspend fun getAllUserPoints(): Flow<List<UserPoint>>

}

interface MarkerRepository {

    suspend fun <T> getAllTSuitRating(rating:Double): Flow<List<T>>

    suspend fun <T> getAllT(): Flow<List<T>>

    suspend fun <T> insert(item: T)

    suspend fun <T> delete(list: List<T>)
}