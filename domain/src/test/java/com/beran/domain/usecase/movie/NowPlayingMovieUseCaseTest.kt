package com.beran.domain.usecase.movie

import com.beran.common.Resource
import com.beran.data.utils.DataDummy
import com.beran.domain.repository.MovieRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NowPlayingMovieUseCaseTest {
    @Mock
    private lateinit var repository: MovieRepository
    private lateinit var useCase: NowPlayingMovieUseCase

    @Before
    fun setUp() {
        useCase = NowPlayingMovieUseCase(repository)
    }

    @Test
    fun `Use case should return success`() = runBlocking {
        val movies = DataDummy.generateMovies()
        Mockito.`when`(repository.getNowPlayingMovies()).thenReturn(movies)
        val actual = useCase.invoke().toList()
        Assert.assertNotNull(actual[1])
        Assert.assertEquals(actual[0], Resource.Loading)
        Assert.assertEquals(actual[1], Resource.Success(movies))
    }

    @Test
    fun `Use case should return failed`() = runBlocking {
        Mockito.`when`(repository.getNowPlayingMovies()).thenThrow(RuntimeException("Failed"))
        val act = useCase.invoke().toList()
        Assert.assertEquals(act[0], Resource.Loading)
        Assert.assertEquals(act[1], Resource.Error("An unexpected error occurred"))
    }
}