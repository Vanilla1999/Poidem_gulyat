package com.example.poidem_gulyat.data.source.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.poidem_gulyat.data.dto.Attraction
import com.example.poidem_gulyat.data.dto.PhotoZone
import com.example.poidem_gulyat.data.dto.UserLocation
import com.example.poidem_gulyat.data.dto.UserPoint
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PhotoZoneDao : BaseDao<PhotoZone>() {

    @Query("SELECT * FROM photoZones ORDER BY rating DESC")
    abstract fun getAllPhotoZones(): Flow<List<PhotoZone>>

    @Query("SELECT * FROM photoZones WHERE rating >=:rating")
    abstract fun getAllPhotoZonesSuitRating(rating:Double): Flow<List<PhotoZone>>
}