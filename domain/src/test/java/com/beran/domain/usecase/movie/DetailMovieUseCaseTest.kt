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
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailMovieUseCaseTest {
    @Mock
    private lateinit var repository: MovieRepository
    private lateinit var useCase: DetailMovieUseCase

    @Before
    fun setUp() {
        useCase = DetailMovieUseCase(repository)
    }

    @Test
    fun `Use case should return success`() = runBlocking {
        val movie = DataDummy.generateMovieDetail()
        Mockito.`when`(repository.getMovieDetail(movie.id)).thenReturn(movie)
        val actual = useCase.invoke(movie.id).toList()
        assertNotNull(actual[1])
        assertEquals(actual[0], Resource.Loading)
        assertEquals(actual[1], Resource.Success(movie))
    }

    @Test
    fun `Use case should return failed`() = runBlocking {
        Mockito.`when`(repository.getMovieDetail(1)).thenThrow(RuntimeException("Failed"))
        val act = useCase.invoke(1).toList()
        assertEquals(act[0], Resource.Loading)
        assertEquals(act[1], Resource.Error("An unexpected error occurred"))
    }
}