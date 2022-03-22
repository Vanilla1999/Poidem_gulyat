package com.example.poidem_gulyat.data.source.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.poidem_gulyat.data.dto.*
import com.example.poidem_gulyat.data.source.database.dao.*


@Database(
    version = 1,
    entities = [
        UserLocation::class,
        MarkerPoint::class,
    ],
    autoMigrations = [
    ],
    exportSchema = true
)
abstract class DatabaseMain : RoomDatabase() {
    abstract fun userLocation(): UserLocationDao
    abstract fun marker(): MarkserDao

    companion object {
        val migrations: Array<Migration> = arrayOf(
        )
        const val DATABASE = "gulyat"
    }
}