package com.example.poidem_gulyat.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "photoZones",
)
data class PhotoZone(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = 0,
    val name:String,
    val latitude: Double,
    val longitude: Double,
    val img: ByteArray?,
    @ColumnInfo(defaultValue = "нет информации")
    val description:String,
    val startWork:Float?,
    val endWork:Float?,
    @ColumnInfo(defaultValue = "0.0")
    val rating: Double
)


