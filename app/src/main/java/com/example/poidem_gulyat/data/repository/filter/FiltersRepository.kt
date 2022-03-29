package com.example.poidem_gulyat.data.repository.filter

import com.example.poidem_gulyat.data.ResponseDataBase
import com.example.poidem_gulyat.data.dto.Filter
import com.example.poidem_gulyat.data.dto.UserLocation
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

interface FiltersRepository {

    suspend fun getFilters(): Flow<ResponseDataBase<Filter>>

    suspend fun insert(item: List<Filter>)

    suspend fun delete(list: List<Filter>)

    suspend fun delete()
}