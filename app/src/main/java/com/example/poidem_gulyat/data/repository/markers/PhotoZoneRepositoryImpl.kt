package com.example.poidem_gulyat.data.repository.markers

import com.example.poidem_gulyat.data.ResponseDataBase
import com.example.poidem_gulyat.data.dto.PhotoZone
import com.example.poidem_gulyat.data.source.database.DatabaseMain
import com.otus.securehomework.data.repository.BaseRepositoryDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class PhotoZoneRepositoryImpl @Inject constructor(
    private val databaseSource: DatabaseMain,
) : PhotoZoneRepository, BaseRepositoryDataBase() {
    override suspend fun getAllPhotoZonesSuitRating(rating: Double): Flow<ResponseDataBase<Any?>> {
        return databaseSource.photoZone().getAllPhotoZonesSuitRating(rating).transform {
            doWork(it, this)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getAllPhotoZones(): Flow<ResponseDataBase<Any?>> {
        return databaseSource.photoZone().getAllPhotoZones().transform {
            doWork(it, this)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun insert(item: PhotoZone) {
       databaseSource.photoZone().insertOrUpdate(item)
    }

    override suspend fun insertList(item: List<PhotoZone>) {
        databaseSource.photoZone().insertOrUpdate(item)
    }

    override suspend fun delete(list: List<PhotoZone>) {
        databaseSource.photoZone().delete(list)
    }
}
