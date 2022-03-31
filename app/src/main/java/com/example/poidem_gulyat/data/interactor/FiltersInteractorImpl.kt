package com.example.poidem_gulyat.data.interactor

import com.example.poidem_gulyat.data.ResponseDataBase
import com.example.poidem_gulyat.data.dto.Filter
import com.example.poidem_gulyat.data.repository.filter.FiltersRepository
import com.example.poidem_gulyat.data.source.database.DatabaseMain
import com.otus.securehomework.data.repository.BaseRepositoryDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

//class FiltersInteractorImpl @Inject constructor(
//    private val filtersRepository: FiltersRepository
//):FiltersInteractor {
//    override suspend fun getFilters(): Flow<List<Filter>> {
//return
//    }
//
//    override suspend fun insert(item: List<Filter>) {
//
//    }
//
//    override suspend fun insertOrIgnore(item: List<Filter>) {
//
//    }
//
//    override suspend fun delete(list: List<Filter>) {
//
//    }
//
//    override suspend fun delete() {
//
//    }
//
//}