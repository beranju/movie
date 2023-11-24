package rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beran.common.Resource
import com.beran.domain.usecase.auth.GetSavedDataUseCase
import com.beran.domain.usecase.movie.NowPlayingMovieUseCase
import com.beran.domain.usecase.movie.PopularMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.state.ListMovieState
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.state.UserState
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val nowPlayingMovieUseCase: NowPlayingMovieUseCase,
    private val popularMovieUseCase: PopularMovieUseCase,
    private val getSavedDataUseCase: GetSavedDataUseCase
) : ViewModel() {

    private var _nowPlayingMovieState = MutableLiveData<ListMovieState>()
    val nowPlayingMovieState: LiveData<ListMovieState> get() = _nowPlayingMovieState

    private var _popMovieState = MutableLiveData<ListMovieState>()
    val popMovieState: LiveData<ListMovieState> get() = _popMovieState

    private var _userState = MutableLiveData<UserState>()
    val userState: LiveData<UserState> get() = _userState

    init {
        getNowPlayingMovies()
        getPopularMovies()
        getSavedData()
    }

    fun getSavedData() {
        getSavedDataUseCase.invoke().onEach { result ->
            when (result) {
                is Resource.Loading -> _userState.value = UserState.Loading
                is Resource.Error -> _userState.value = UserState.Error(result.error)
                is Resource.Success -> _userState.value = UserState.Success(result.data)
            }

        }.launchIn(viewModelScope)
    }

    private fun getNowPlayingMovies() {
        nowPlayingMovieUseCase().onEach { result ->
            when (result) {
                is Resource.Loading -> _nowPlayingMovieState.value = ListMovieState.Loading
                is Resource.Error -> _nowPlayingMovieState.value =
                    ListMovieState.Error(result.error)

                is Resource.Success -> _nowPlayingMovieState.value =
                    ListMovieState.Success(result.data)
            }
        }.launchIn(viewModelScope)
    }

    private fun getPopularMovies() {
        popularMovieUseCase().onEach { result ->
            when (result) {
                is Resource.Loading -> _popMovieState.value = ListMovieState.Loading
                is Resource.Error -> _popMovieState.value =
                    ListMovieState.Error(result.error)

                is Resource.Success -> _popMovieState.value =
                    ListMovieState.Success(result.data)
            }
        }.launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}