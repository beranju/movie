package com.beran.data.local.model

import androidx.room.Entity

@Entity(tableName = "movie")
data class MovieEntity(
    val id: Int,
    val overview: String,
    val title: String,
    val posterPath: String,
    val backdropPath: String,
    val releaseDate: String,
    val popularity: Double,
    val voteAverage: Double,
    val voteCount: Int,
    val revenue: Int,
    val genres: String,
    val budget: Int,
    val tagline: String,
    val homepage: String,
    val status: String
)
