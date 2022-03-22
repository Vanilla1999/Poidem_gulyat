package com.example.poidem_gulyat.data.source.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.poidem_gulyat.data.dto.*
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MarkserDao : BaseDao<MarkerPoint>() {

    @Query("SELECT * FROM markers ORDER BY rating DESC")
    abstract fun getAllMarksers(): Flow<List<MarkerPoint>>

    @Query("SELECT * FROM markers WHERE rating >=:rating ORDER BY rating DESC")
    abstract fun getAllMarkerSuitRating(rating:Double): Flow<List<MarkerPoint>>

    @Query("SELECT * FROM markers WHERE type ==:type  ORDER BY rating DESC")
    abstract fun getAllMarkersByType(type:Int): Flow<List<MarkerPoint>>

    @Query("SELECT * FROM markers WHERE rating >=:rating AND type ==:type ORDER BY rating DESC")
    abstract fun getAllMarkersByTypeSuitRating(type:Int,rating:Double): Flow<List<MarkerPoint>>

}