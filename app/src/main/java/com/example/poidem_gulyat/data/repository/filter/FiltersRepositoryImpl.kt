package com.example.poidem_gulyat.data.repository.filter

import com.example.poidem_gulyat.data.ResponseDataBase
import com.example.poidem_gulyat.data.dto.Filter
import com.example.poidem_gulyat.data.dto.UserLocation
import com.example.poidem_gulyat.data.source.database.DatabaseMain
import com.otus.securehomework.data.repository.BaseRepositoryDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class FiltersRepositoryImpl @Inject constructor(
    private val databaseSource: DatabaseMain
):FiltersRepository, BaseRepositoryDataBase() {
    override  fun getFilters(): Flow<ResponseDataBase<Filter>> {
      return  databaseSource.filters().getFilter().transform {
          doWorkNotList(it, Filter(),this)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun insert(item: Filter) {
        databaseSource.filters().insertOrUpdate(item)
    }

    override suspend fun insertOrIgnore(item: Filter) {
        databaseSource.filters().insertOrIgnore(item)
    }

    override suspend fun delete() {
        databaseSource.filters().nukeTable()
    }
}