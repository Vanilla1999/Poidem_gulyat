package com.example.poidem_gulyat.data.source.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.poidem_gulyat.data.dto.Attraction
import com.example.poidem_gulyat.data.dto.UserLocation
import com.example.poidem_gulyat.data.dto.UserPoint
import kotlinx.coroutines.flow.Flow

@Dao
abstract class AttractionDao : BaseDao<Attraction>() {

    @Query("SELECT * FROM attractions ORDER BY rating DESC")
    abstract fun getAllAttractions(): Flow<List<Attraction>>

    @Query("SELECT * FROM attractions WHERE rating >=:rating")
    abstract fun getAllAttractionSuitRating(rating:Double): Flow<List<Attraction>>
}