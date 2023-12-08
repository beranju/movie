package com.beran.data.repository

import com.beran.data.local.pref.SessionManager
import com.beran.data.network.model.Data
import com.beran.data.network.model.ImgBbResponse
import com.beran.data.network.retrofit.ImgBbApi
import com.beran.domain.model.UserData
import com.beran.domain.repository.AuthRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class AuthRepositoryImplTest {
    @Mock
    private lateinit var sessionManager: SessionManager

    @Mock
    private lateinit var file: File

    @Mock
    private lateinit var imgApi: ImgBbApi
    private lateinit var repository: AuthRepository

    private val user = UserData(
        name = "ber",
        username = "b",
        birthDay = "123456",
        imageUrl = "img",
        address = "jln"
    )

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        file = mock(File::class.java)
        repository = AuthRepositoryImpl(sessionManager = sessionManager, api = imgApi)
    }

    @Test
    fun `when user is login should return true`() = runTest {
        val expected = flow { emit(true) }
        `when`(sessionManager.isLogin()).thenReturn(expected)
        val actual = repository.isLogin()
        assertEquals(expected, actual)
    }

    @Test
    fun `when user is not login should return false`() = runTest {
        val expected = flow { emit(false) }
        `when`(sessionManager.isLogin()).thenReturn(expected)
        val actual = repository.isLogin()
        assertEquals(expected, actual)
    }

    @Test
    fun `createSession should delegate to session manager`() = runTest {
        `when`(sessionManager.createSession()).thenReturn(Unit)
        repository.createSession()
        verify(sessionManager).createSession()
    }

    @Test
    fun `getSavedData should return userdata`() {
        val expected = flowOf(user)
        `when`(sessionManager.getUser()).thenReturn(expected)
        val actual = repository.getSavedData()
        assertNotNull(actual)
        assertEquals(expected, actual)
    }

    @Test
    fun saveUser() = runTest {
        `when`(sessionManager.saveUser(user)).thenReturn(Unit)
        repository.saveUser(user)
        verify(sessionManager).saveUser(user)
    }

    @Test
    fun updateUserImage() = runTest {
        `when`(sessionManager.updateUserImage("imgUrl")).thenReturn(Unit)
        repository.updateUserImage("imgUrl")
        verify(sessionManager).updateUserImage("imgUrl")
    }

//    @Test
//    fun uploadPhoto() = runTest {
//        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
//        val body = MultipartBody.Part.createFormData(
//            "image",
//            file.name,
//            requestFile
//        )
//        val response = Response.success(ImgBbResponse(data = Data(url = "imgUrl")))
//        `when`(imgApi.uploadPhoto(image = body)).thenReturn(response)
//        val actual = repository.uploadPhoto(file)
//        verify(imgApi).uploadPhoto(image = body)
//        assertEquals("imgUrl", actual.orEmpty())
//    }

    @Test
    fun logOut() = runTest {
        `when`(sessionManager.logout()).thenReturn(Unit)
        repository.logOut()
        verify(sessionManager).logout()
    }
}