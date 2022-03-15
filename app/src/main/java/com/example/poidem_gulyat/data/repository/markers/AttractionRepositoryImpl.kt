package com.example.poidem_gulyat.data.repository.markers

import com.example.poidem_gulyat.data.dto.Attraction
import com.example.poidem_gulyat.data.source.database.DatabaseMain
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AttractionRepositoryImpl @Inject constructor(
    private val databaseSource: DatabaseMain
):AttractionRepository {
    override suspend fun getAllAttractionsSuitRating(rating: Double): Flow<List<Attraction>> {
        return databaseSource.attraction().getAllAttractionSuitRating(rating)
    }

    override suspend fun getAllAttractions(): Flow<List<Attraction>> {

      return databaseSource.attraction().getAllAttractions()
    }

    override suspend fun insert(item: Attraction) {
        databaseSource.attraction().insertOrUpdate(item)
    }

    override suspend fun delete(list: List<Attraction>) {
        databaseSource.attraction().delete(list)
    }
}