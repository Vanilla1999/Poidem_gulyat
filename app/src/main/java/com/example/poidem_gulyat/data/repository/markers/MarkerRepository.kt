package com.example.poidem_gulyat.data.repository.markers


import com.example.poidem_gulyat.data.ResponseDataBase
import com.example.poidem_gulyat.data.dto.Filter

import com.example.poidem_gulyat.data.dto.MarkerPoint

import kotlinx.coroutines.flow.Flow

interface MainRepo <T>{
    suspend fun insert(item: T)

    suspend fun insertList(item:List<T>)

    suspend fun delete(list: List<T>)
}

interface MarkerRepository :MainRepo<MarkerPoint> {

    suspend fun getAllMarkersSuitRating(rating:Double): Flow<ResponseDataBase<MarkerPoint>>

    suspend fun getAllMarksers(): Flow<ResponseDataBase<MarkerPoint>>

     fun getAllMarksersWithFilter(filter:Filter): Flow<ResponseDataBase<MarkerPoint>>

    suspend fun getAllMarkersByType(type:Int): Flow<ResponseDataBase<MarkerPoint>>

    suspend fun getAllMarkersByTypeSuitRating(type:Int,rating:Double): Flow<ResponseDataBase<MarkerPoint>>
}
