package com.example.poidem_gulyat.data.repository.filter

import androidx.room.Query
import com.example.poidem_gulyat.data.ResponseDataBase
import com.example.poidem_gulyat.data.dto.Filter
import com.example.poidem_gulyat.data.dto.UserLocation
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

interface FiltersRepository {

     fun getFilters(): Flow<ResponseDataBase<Filter>>

    suspend fun insert(item: Filter)

    suspend fun insertOrIgnore(item: Filter)

    suspend fun delete()
}