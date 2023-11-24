package rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beran.common.Resource
import com.beran.domain.model.UserData
import com.beran.domain.usecase.auth.CreateSessionUseCase
import com.beran.domain.usecase.auth.SaveDataProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.state.AuthState
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val createSessionUseCase: CreateSessionUseCase,
    private val saveDataProfileUseCase: SaveDataProfileUseCase
) : ViewModel() {
    private var _saveDataState = MutableLiveData<AuthState>()
    val saveDataState: LiveData<AuthState> get() = _saveDataState

    private var _sessionState = MutableLiveData<AuthState>()
    val sessionState: LiveData<AuthState> get() = _sessionState


    fun createSession() {
        createSessionUseCase.invoke().onEach { result ->
            when (result) {
                is Resource.Loading -> _sessionState.value = AuthState.Loading
                is Resource.Error -> _sessionState.value = AuthState.Error(result.error)
                is Resource.Success -> _sessionState.value = AuthState.Success
            }
        }.launchIn(viewModelScope)
    }

    fun saveData(userData: UserData) =
        saveDataProfileUseCase.invoke(userData).onEach { result ->
            when (result) {
                is Resource.Loading -> _saveDataState.value = AuthState.Loading
                is Resource.Error -> _saveDataState.value = AuthState.Error(result.error)
                is Resource.Success -> _saveDataState.value = AuthState.Success
            }
        }.launchIn(viewModelScope)

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}