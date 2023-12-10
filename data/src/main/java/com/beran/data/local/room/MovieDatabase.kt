package com.beran.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.beran.data.local.model.MovieEntity

@Database(
    entities = [MovieEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}