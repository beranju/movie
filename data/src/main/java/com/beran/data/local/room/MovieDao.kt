package com.beran.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.beran.data.local.model.MovieEntity

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie")
    fun getAll(): List<MovieEntity>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun getById(id: Int): MovieEntity

    @Query("SELECT EXISTS(SELECT * FROM movie WHERE id = :id)")
    fun isFavoriteMovie(id: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movieEntity: MovieEntity)

    @Delete
    suspend fun deleteMovie(movie: MovieEntity)

}