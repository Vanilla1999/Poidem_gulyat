package com.example.poidem_gulyat.data.repository.location

import com.example.poidem_gulyat.data.dto.UserLocation
import com.example.poidem_gulyat.data.source.database.DatabaseMain
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserLocationRepositoryImpl @Inject constructor(
    private val databaseSource: DatabaseMain
):UserLocationRepository {
    override suspend fun getLastLocation(): Flow<UserLocation> {
        return  databaseSource.userLocation().getLastLocation()
    }

    override suspend fun getAllUserLocations(): Flow<List<UserLocation>> {
       return databaseSource.userLocation().getAllUserLocation()
    }

    override suspend fun insert(item: UserLocation) {
        databaseSource.userLocation().insertOrUpdate(item)
    }

    override suspend fun delete(list: List<UserLocation>) {
        databaseSource.userLocation().delete(list)
    }
}