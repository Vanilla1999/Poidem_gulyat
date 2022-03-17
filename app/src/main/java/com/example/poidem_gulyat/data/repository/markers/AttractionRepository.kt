package com.example.poidem_gulyat.data.repository.markers

import com.example.poidem_gulyat.data.ResponseDataBase
import com.example.poidem_gulyat.data.dto.Attraction
import com.example.poidem_gulyat.data.dto.PhotoZone
import kotlinx.coroutines.flow.Flow

interface AttractionRepository:MainRepo<Attraction> {
    suspend fun getAllAttractionsSuitRating(rating:Double): Flow<ResponseDataBase<Any?>>

    suspend fun getAllAttractions(): Flow<ResponseDataBase<Any?>>

}