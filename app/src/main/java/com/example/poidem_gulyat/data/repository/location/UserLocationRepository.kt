package com.example.poidem_gulyat.data.repository.location

import com.example.poidem_gulyat.data.dto.UserLocation
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

interface UserLocationRepository {

    suspend fun getLastLocation(): Flow<UserLocation>

    suspend fun getAllUserLocations(): Flow<List<UserLocation>>

    suspend fun insert(item: UserLocation)

    suspend fun delete(list: List<UserLocation>)

}