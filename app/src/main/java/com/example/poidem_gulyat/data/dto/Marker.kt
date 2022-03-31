package com.example.poidem_gulyat.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "markers"
)
data class MarkerPoint(
    @PrimaryKey
    val id: Int = 0,
    val name:String,
    val latitude: Double,
    val longitude: Double,
    val img: ByteArray?,
    @ColumnInfo(defaultValue = "нет информации")
    val description:String,
    val startWork:Long?,
    val endWork:Long?,
    @ColumnInfo(defaultValue = "0.0")
    val rating: Float?,
    val adress:String = "г Красноадр, ул. Кривой Рог, д.19",
    val price:Int,
    val type:Int
)

