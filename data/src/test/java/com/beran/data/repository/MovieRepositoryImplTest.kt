package com.beran.data.repository

import com.beran.data.local.room.MovieDao
import com.beran.data.mappers.toMovieDetail
import com.beran.data.mappers.toMovieItems
import com.beran.data.network.model.MovieDetailResponse
import com.beran.data.network.model.NowPlayingResponse
import com.beran.data.network.model.PopularResponse
import com.beran.data.network.retrofit.MovieApi
import com.beran.data.utils.DataDummy
import com.beran.domain.repository.MovieRepository
import junit.framework.TestCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class MovieRepositoryImplTest {
    @Mock
    private lateinit var movieDao: MovieDao

    @Mock
    private lateinit var api: MovieApi
    private lateinit var repo: MovieRepository

    @Before
    fun setUp() {
        repo = MovieRepositoryImpl(movieDao, api)
    }

    @Test
    fun `getNowPlayingMovies should return success`() = runTest {
        val listMovies = DataDummy.generateMovies()
        val response = Response.success(
            NowPlayingResponse(
                results = listMovies
            )
        )
        `when`(api.getNowPlayingMovies()).thenReturn(response)

        val expected = listMovies.toMovieItems()

        val actual = repo.getNowPlayingMovies()

        verify(api).getNowPlayingMovies()
        assertEquals(expected, actual)
    }

    @Test
    fun `getNowPlayingMovies should return error`() = runTest {
        `when`(api.getNowPlayingMovies()).thenThrow(RuntimeException("cannot process"))
        try {
            repo.getNowPlayingMovies()
            TestCase.fail()
        } catch (e: Exception) {
            assertTrue(e is RuntimeException)
            assertEquals(e.message, "cannot process")
        }
    }

    @Test
    fun `getPopularMovies should return data`() = runTest {
        val listMovies = DataDummy.generateMovies()
        val response = Response.success(
            PopularResponse(
                results = listMovies
            )
        )
        `when`(api.getPopularMovies()).thenReturn(response)

        val expected = listMovies.toMovieItems()

        val actual = repo.getPopularMovies()

        verify(api).getPopularMovies()
        assertEquals(expected, actual)
    }

    @Test
    fun `getPopularMovies should return error`() = runTest {
        `when`(api.getPopularMovies()).thenThrow(RuntimeException("cannot process"))
        try {
            repo.getPopularMovies()
            TestCase.fail()
        } catch (e: Exception) {
            assertTrue(e is RuntimeException)
            assertEquals(e.message, "cannot process")
        }
    }

    @Test
    fun `getMovieDetail should return data`() = runTest {
        val movie = MovieDetailResponse(id = 1)
        val response = Response.success(movie)
        `when`(api.getDetailMovie(1)).thenReturn(response)
        val expected = movie.toMovieDetail()

        val actual = repo.getMovieDetail(1)

        verify(api).getDetailMovie(1)
        assertNotNull(actual)
        assertEquals(expected, actual)
    }

    @Test
    fun `getMovieDetail should return error`() = runTest {
        `when`(api.getDetailMovie(1)).thenThrow(RuntimeException("cannot process"))
        try {
            repo.getMovieDetail(1)
            TestCase.fail()
        } catch (e: Exception) {
            assertTrue(e is RuntimeException)
            assertEquals(e.message, "cannot process")
        }
    }
}