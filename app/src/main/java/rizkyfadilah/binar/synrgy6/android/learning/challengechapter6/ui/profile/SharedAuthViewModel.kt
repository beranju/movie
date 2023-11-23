package rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beran.common.Resource
import com.beran.domain.model.UserData
import com.beran.domain.usecase.auth.CreateSessionUseCase
import com.beran.domain.usecase.auth.GetSavedDataUseCase
import com.beran.domain.usecase.auth.LogOutUseCase
import com.beran.domain.usecase.auth.SaveDataProfileUseCase
import com.beran.domain.usecase.auth.UploadPhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.AuthState
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.UploadState
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.UserState
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SharedAuthViewModel @Inject constructor(
    private val savedDataUseCase: SaveDataProfileUseCase,
    private val getSavedDataUseCase: GetSavedDataUseCase,
    private val createSessionUseCase: CreateSessionUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val uploadPhotoUseCase: UploadPhotoUseCase
) : ViewModel() {

    private var _userState = MutableLiveData<UserState>()
    val userState: LiveData<UserState> get() = _userState

    private var _sessionState = MutableLiveData<AuthState>()
    val sessionState: LiveData<AuthState> get() = _sessionState

    private var _saveDataState = MutableLiveData<AuthState>()
    val saveDataState: LiveData<AuthState> get() = _saveDataState

    private var _logOutState = MutableLiveData<AuthState>()
    val logOutState: LiveData<AuthState> get() = _logOutState

    private var _uploadState = MutableLiveData<UploadState>()
    val uploadState: LiveData<UploadState> get() = _uploadState

    init {
        getSavedData()
    }

    private fun getSavedData() =
        getSavedDataUseCase.invoke().onEach { result ->
            when (result) {
                is Resource.Loading -> _userState.value = UserState.Loading
                is Resource.Error -> _userState.value = UserState.Error(result.error)
                is Resource.Success -> _userState.value = UserState.Success(result.data)
            }

        }.launchIn(viewModelScope)


    fun saveData(userData: UserData) =
        savedDataUseCase.invoke(userData).onEach { result ->
            when (result) {
                is Resource.Loading -> _saveDataState.value = AuthState.Loading
                is Resource.Error -> _saveDataState.value = AuthState.Error(result.error)
                is Resource.Success -> _saveDataState.value = AuthState.Success
            }
        }.launchIn(viewModelScope)


    fun createSession() {
        createSessionUseCase.invoke().onEach { result ->
            when (result) {
                is Resource.Loading -> _sessionState.value = AuthState.Loading
                is Resource.Error -> _sessionState.value = AuthState.Error(result.error)
                is Resource.Success -> _sessionState.value = AuthState.Success
            }
        }.launchIn(viewModelScope)
    }

    fun uploadPhoto(file: File){
        uploadPhotoUseCase.invoke(file).onEach { result ->
            when (result) {
                is Resource.Loading -> _uploadState.value = UploadState.Loading
                is Resource.Error -> _uploadState.value = UploadState.Error(result.error)
                is Resource.Success -> _uploadState.value = UploadState.Success(result.data)
            }
        }.launchIn(viewModelScope)
    }

    fun logOut(){
        logOutUseCase().onEach { result ->
            when (result) {
                is Resource.Loading -> _logOutState.value = AuthState.Loading
                is Resource.Error -> _logOutState.value = AuthState.Error(result.error)
                is Resource.Success -> _logOutState.value = AuthState.Success
            }
        }.launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}