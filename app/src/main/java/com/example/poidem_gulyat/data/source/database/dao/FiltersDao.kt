package com.example.poidem_gulyat.data.source.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.poidem_gulyat.data.dto.Filter
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FiltersDao : BaseDao<Filter>() {

    @Query("SELECT * FROM filters")
    abstract fun getFilter(): Flow<List<Filter>>

    @Query("DELETE FROM filters")
    abstract fun nukeTable()

}