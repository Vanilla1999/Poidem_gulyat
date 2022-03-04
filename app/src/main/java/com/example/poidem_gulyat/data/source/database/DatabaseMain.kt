package com.example.poidem_gulyat.data.source.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.poidem_gulyat.data.dto.UserLocation
import com.example.poidem_gulyat.data.source.database.dao.UserLocationDao


@Database(
    version = 1,
    entities = [
        UserLocation::class
    ],
    exportSchema = true
)
abstract class DatabaseMain: RoomDatabase() {
    abstract fun userLocation(): UserLocationDao
    companion object {
        const val DATABASE = "gulyat"
        }
}