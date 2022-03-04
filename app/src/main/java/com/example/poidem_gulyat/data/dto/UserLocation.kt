package com.example.poidem_gulyat.data.dto

import androidx.room.Entity

@Entity(
    tableName = "user_location",
    primaryKeys = ["time"]
)
data class UserLocation(
    val time: Long,
    val latitude: Double,
    val longitude: Double,
    val provider: String,
    val accuracy: Float
)