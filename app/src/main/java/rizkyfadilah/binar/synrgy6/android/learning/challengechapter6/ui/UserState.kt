package rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui

import com.beran.domain.model.UserData

sealed interface UserState {
    data object Loading : UserState
    data class Success(val userData: UserData) : UserState
    data class Error(val error: String) : UserState
}

sealed interface AuthState {
    data object Loading : AuthState
    data object Success : AuthState
    data class Error(val error: String) : AuthState
}

sealed interface UploadState {
    data object Loading : UploadState
    data class Success(val url: String) : UploadState
    data class Error(val error: String) : UploadState
}