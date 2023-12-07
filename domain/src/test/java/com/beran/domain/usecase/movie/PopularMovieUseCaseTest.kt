package com.beran.domain.usecase.movie

import com.beran.common.Resource
import com.beran.data.utils.DataDummy
import com.beran.domain.repository.MovieRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PopularMovieUseCaseTest {
    @Mock
    private lateinit var repository: MovieRepository
    private lateinit var useCase: PopularMovieUseCase

    @Before
    fun setup() {
        useCase = PopularMovieUseCase(repository)
    }

    @Test
    fun `Use case should return success`() = runBlocking {
        val movies = DataDummy.generateMovies()
        `when`(repository.getPopularMovies()).thenReturn(movies)
        val actual = useCase.invoke().toList()
        assertNotNull(actual[1])
        assertEquals(actual[0], Resource.Loading)
        assertEquals(actual[1], Resource.Success(movies))
    }

    @Test
    fun `Use case should return failed`() = runBlocking {
        `when`(repository.getPopularMovies()).thenThrow(RuntimeException("Failed"))
        val act = useCase.invoke().toList()
        assertEquals(act[0], Resource.Loading)
        assertEquals(act[1], Resource.Error("An unexpected error occurred"))
//        try {
//            val act = useCase.invoke().toList()
//            assertEquals(act[0], Resource.Loading)
//            assertEquals(act[1], Resource.Error("An unexpected error occurred"))
//        }catch (e: Exception){
//            assertTrue(e is RuntimeException)
//            assertEquals(e.message, "Failed")
//        }
    }
}