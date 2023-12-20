package rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.beran.common.Constants.IMAGE_MANIPULATION_WORK_NAME
import com.beran.common.Constants.KEY_IMAGE_URI
import com.beran.common.Constants.TAG_OUTPUT
import com.beran.common.Constants.TAG_PROGRESS
import com.beran.common.Resource
import com.beran.domain.model.UserData
import com.beran.domain.usecase.auth.GetSavedDataUseCase
import com.beran.domain.usecase.auth.LogOutUseCase
import com.beran.domain.usecase.auth.SaveDataProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.state.AuthState
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.state.UserState
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.worker.BlurWorker
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.worker.CleanupWorker
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.worker.SaveDataWorker
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.worker.UploadWorker
import javax.inject.Inject

@HiltViewModel
class SharedAuthViewModel @Inject constructor(
    private val savedDataUseCase: SaveDataProfileUseCase,
    private val getSavedDataUseCase: GetSavedDataUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val workManager: WorkManager
) : ViewModel() {

    private var _userState = MutableLiveData<UserState>()
    val userState: LiveData<UserState> get() = _userState

    private var _saveDataState = MutableLiveData<AuthState>()
    val saveDataState: LiveData<AuthState> get() = _saveDataState

    private var _logOutState = MutableLiveData<AuthState>()
    val logOutState: LiveData<AuthState> get() = _logOutState

    val outputWorkInfos: LiveData<List<WorkInfo>>
    val progressWorkInfos: LiveData<List<WorkInfo>>

    init {
        getSavedData()
        outputWorkInfos = workManager.getWorkInfosByTagLiveData(TAG_OUTPUT)
        progressWorkInfos = workManager.getWorkInfosByTagLiveData(TAG_PROGRESS)
    }

    fun getSavedData() =
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
                is Resource.Success -> {
                    getSavedData()
                    _saveDataState.value = AuthState.Success
                }
            }
        }.launchIn(viewModelScope)

    fun blurImage(uri: Uri) {
        val imageUri = Data.Builder()
            .putString(KEY_IMAGE_URI, uri.toString())
            .build()
        var continuation = workManager.beginUniqueWork(
            IMAGE_MANIPULATION_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequest.from(CleanupWorker::class.java)
        )

        val blurRequest = OneTimeWorkRequestBuilder<BlurWorker>()
            .setInputData(imageUri)
            .addTag(TAG_PROGRESS)
            .build()

        continuation = continuation.then(blurRequest)

        val upload = OneTimeWorkRequestBuilder<UploadWorker>()
            .addTag(TAG_PROGRESS)
            .build()
        continuation = continuation.then(upload)
        val save = OneTimeWorkRequestBuilder<SaveDataWorker>()
            .addTag(TAG_OUTPUT)
            .build()
        continuation = continuation.then(save)

        continuation.enqueue()
    }

    fun cancelBlur() {
        workManager.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)
    }

    fun logOut() {
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