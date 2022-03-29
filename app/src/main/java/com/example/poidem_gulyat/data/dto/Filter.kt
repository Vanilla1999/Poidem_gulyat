package com.example.poidem_gulyat.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

// 0 - нет поля 1 есть поле, но не выбрано 2 - выбрано.
@Entity(
    tableName = "filters"
)
data class Filter(
    @PrimaryKey
    val id:Int,
    var sortByRating: Int = 0,
    var bestNearby: Int = 0,
    var rating: Int = 0,
    var attraction: Int = 0,
    var photoZone: Int = 0,
    var userPoint: Int = 0,
    var paid: Int = 0,
    var open: Int = 0,
    var close: Int = 0,
    var free: Int = 0
)
