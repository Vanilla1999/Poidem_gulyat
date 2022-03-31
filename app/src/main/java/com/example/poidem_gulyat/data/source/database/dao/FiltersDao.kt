package com.example.poidem_gulyat.data.source.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.poidem_gulyat.data.dto.Filter
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FiltersDao : BaseDao<Filter>() {

    @Query("SELECT * FROM filters")
    abstract fun getFilter(): Flow<Filter>

    @Query("UPDATE filters SET typeSort = :typeSort ")
    abstract fun updateTypeSort(typeSort:Int)

    @Query("UPDATE filters SET typeMarker = :typeMarker")
    abstract fun updateType(typeMarker:Int)

    @Query("UPDATE filters SET open = :open")
    abstract fun updateOpen(open:Int)

    @Query("UPDATE filters SET price = :price")
    abstract fun updatePrice(price:Int)


    @Query("DELETE FROM filters")
    abstract fun nukeTable()

}