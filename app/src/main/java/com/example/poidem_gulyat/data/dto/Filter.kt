package com.example.poidem_gulyat.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

// 0 - нет поля 1 есть поле, но не выбрано 2 - выбрано.
@Entity(
    tableName = "filters"
)
data class Filter(
    @PrimaryKey
    val id: Int = 0,
    var typeSort: Int = 0,
    var rating: Int = 0,
    var typeMarker: Int = 0,
    var price: Int = 0,
    var open: Int = 0,
)
