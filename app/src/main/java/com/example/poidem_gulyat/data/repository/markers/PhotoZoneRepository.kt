package com.example.poidem_gulyat.data.repository.markers

import com.example.poidem_gulyat.data.ResponseDataBase
import com.example.poidem_gulyat.data.dto.Attraction
import com.example.poidem_gulyat.data.dto.PhotoZone
import com.example.poidem_gulyat.data.dto.UserPoint
import kotlinx.coroutines.flow.Flow

interface PhotoZoneRepository:MainRepo<PhotoZone>  {
    suspend fun getAllPhotoZonesSuitRating(rating:Double): Flow<ResponseDataBase<Any?>>

    suspend fun getAllPhotoZones(): Flow<ResponseDataBase<Any?>>
}