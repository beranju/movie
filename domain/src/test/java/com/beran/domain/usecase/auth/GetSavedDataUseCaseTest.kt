package com.beran.domain.usecase.auth

import com.beran.common.Resource
import com.beran.domain.model.UserData
import com.beran.domain.repository.AuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class GetSavedDataUseCaseTest {
    @Mock
    private lateinit var repository: AuthRepository
    private lateinit var getSavedDataUseCase: GetSavedDataUseCase

    @Before
    fun setUp() {
        getSavedDataUseCase = GetSavedDataUseCase(repository)
    }

    @Test
    fun `use case should return user data`() = runTest {
        val user = UserData(
            name = "ber",
            username = "b",
            birthDay = "123456",
            imageUrl = "img",
            address = "jln"
        )
        val expected = flowOf(user)
        `when`(repository.getSavedData()).thenReturn(expected)
        val actual = getSavedDataUseCase.invoke().toList()
        verify(repository).getSavedData()
        assertEquals(Resource.Loading, actual[0])
        assertEquals(Resource.Success(user), actual[1])
    }

    @Test
    fun `use case should return failed`() = runTest {
        `when`(repository.getSavedData()).thenThrow(RuntimeException("Failed"))
        val actual = getSavedDataUseCase.invoke().toList()
        verify(repository).getSavedData()
        assertEquals(Resource.Loading, actual[0])
        assertEquals(Resource.Error("An unexpected error occurred"), actual[1])
    }
}