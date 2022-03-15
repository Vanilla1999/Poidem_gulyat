package com.example.poidem_gulyat.data.repository.markers

import com.example.poidem_gulyat.data.dto.Attraction
import com.example.poidem_gulyat.data.dto.PhotoZone
import kotlinx.coroutines.flow.Flow

interface AttractionRepository {
    suspend fun getAllAttractionsSuitRating(rating:Double): Flow<List<Attraction>>

    suspend fun getAllAttractions(): Flow<List<Attraction>>

    suspend fun insert(item: Attraction)

    suspend fun delete(list: List<Attraction>)
}