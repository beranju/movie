package com.beran.data.mappers

import com.beran.data.local.model.MovieEntity
import com.beran.data.network.model.MovieDetailResponse
import com.beran.data.network.model.ResultsItem
import com.beran.domain.model.MovieDetail
import com.beran.domain.model.MovieItem

fun List<ResultsItem>.toMovieItems(): List<MovieItem> {
    return map {
        MovieItem(
            id = it.id ?: 0,
            voteCount = it.voteCount ?: 0,
            overview = it.overview.orEmpty(),
            originalTitle = it.originalTitle.orEmpty(),
            title = it.title.orEmpty(),
            genreIds = it.genreIds?.map { id -> id ?: 0 }.orEmpty(),
            posterPath = it.posterPath.orEmpty(),
            backdropPath = it.backdropPath.orEmpty(),
            releaseDate = it.releaseDate.orEmpty(),
            popularity = it.popularity.toString().toDoubleOrNull() ?: 0.0,
            voteAverage = it.voteAverage.toString().toDoubleOrNull() ?: 0.0
        )
    }
}

fun List<MovieEntity>.toMovieItems(): List<MovieItem> {
    return map {
        MovieItem(
            id = it.id,
            voteCount = it.voteCount,
            overview = it.overview,
            originalTitle = it.title,
            title = it.title,
            genreIds = listOf(0),
            posterPath = it.posterPath,
            backdropPath = it.backdropPath,
            releaseDate = it.releaseDate,
            popularity = it.popularity,
            voteAverage = it.voteAverage
        )
    }
}

fun MovieDetailResponse.toMovieDetail(): MovieDetail {
    return MovieDetail(
        title = title.orEmpty(),
        backdropPath = backdropPath.orEmpty(),
        revenue = revenue ?: 0,
        genres = genres?.map { it?.name.orEmpty() }.toString(),
        popularity = popularity.toString().toDoubleOrNull() ?: 0.0,
        id = id ?: 0,
        voteCount = voteCount ?: 0,
        budget = budget ?: 0,
        overview = overview.orEmpty(),
        originalTitle = originalTitle.orEmpty(),
        posterPath = posterPath.orEmpty(),
        releaseDate = releaseDate.orEmpty(),
        voteAverage = voteAverage.toString().toDoubleOrNull() ?: 0.0,
        tagline = tagline.orEmpty(),
        homepage = homepage.orEmpty(),
        status = status.orEmpty()
    )
}

fun MovieDetail.toMovieEntity(): MovieEntity {
    return MovieEntity(
        id,
        overview,
        title,
        posterPath,
        backdropPath,
        releaseDate,
        popularity,
        voteAverage,
        voteCount,
        revenue,
        genres,
        budget,
        tagline,
        homepage,
        status
    )
}

fun MovieEntity.toMovieDetail(): MovieDetail {
    return MovieDetail(
        title = title,
        backdropPath = backdropPath,
        revenue = revenue,
        genres = genres,
        popularity = popularity,
        id = id,
        voteCount = voteCount,
        budget = budget,
        overview = overview,
        originalTitle = title,
        posterPath = posterPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        tagline = tagline,
        homepage = homepage,
        status = status
    )
}