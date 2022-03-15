package com.example.poidem_gulyat.data.source.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.poidem_gulyat.data.dto.Attraction
import com.example.poidem_gulyat.data.dto.PhotoZone
import com.example.poidem_gulyat.data.dto.UserLocation
import com.example.poidem_gulyat.data.dto.UserPoint
import com.example.poidem_gulyat.data.source.database.dao.AttractionDao
import com.example.poidem_gulyat.data.source.database.dao.PhotoZoneDao
import com.example.poidem_gulyat.data.source.database.dao.UserLocationDao
import com.example.poidem_gulyat.data.source.database.dao.UserPointDao


@Database(
    version = 2,
    entities = [
        UserLocation::class,
        PhotoZone::class,
        Attraction::class,
        UserPoint::class
    ],
    exportSchema = true
)
abstract class DatabaseMain: RoomDatabase() {
    abstract fun userLocation(): UserLocationDao
    abstract fun photoZone(): PhotoZoneDao
    abstract fun userPoint(): UserPointDao
    abstract fun attraction(): AttractionDao
    companion object {
        val migrations: Array<Migration> = arrayOf(
            object : Migration(1, 2) { override fun migrate(database: SupportSQLiteDatabase) {} }
        )
        const val DATABASE = "gulyat"
        }
}