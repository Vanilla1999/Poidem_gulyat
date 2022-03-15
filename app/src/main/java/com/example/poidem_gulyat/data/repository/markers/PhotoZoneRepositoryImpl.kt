package com.example.poidem_gulyat.data.repository.markers

import com.example.poidem_gulyat.data.dto.PhotoZone
import com.example.poidem_gulyat.data.source.database.DatabaseMain
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PhotoZoneRepositoryImpl @Inject constructor(
    private val databaseSource: DatabaseMain,
) : PhotoZoneRepository {
    override suspend fun getAllPhotoZonesSuitRating(rating: Double): Flow<List<PhotoZone>> {
        return databaseSource.photoZone().getAllPhotoZonesSuitRating(rating)
    }

    override suspend fun getAllPhotoZones(): Flow<List<PhotoZone>> {
        return databaseSource.photoZone().getAllPhotoZones()
    }

    override suspend fun insert(item: PhotoZone) {
       databaseSource.photoZone().insertOrUpdate(item)
    }

    override suspend fun delete(list: List<PhotoZone>) {
        databaseSource.photoZone().delete(list)
    }
}