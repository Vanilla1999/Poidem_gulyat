package com.example.poidem_gulyat.data.source.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.poidem_gulyat.data.dto.UserLocation
import com.example.poidem_gulyat.data.dto.UserPoint
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserPointDao : BaseDao<UserPoint>() {

    @Query("SELECT * FROM userPoints ORDER BY rating DESC")
    abstract fun getAllUserPoints(): Flow<List<UserPoint>>

    @Query("SELECT * FROM userPoints WHERE rating >=:rating")
    abstract fun getAllUserPointsSuitRating(rating:Double): Flow<List<UserPoint>>
}