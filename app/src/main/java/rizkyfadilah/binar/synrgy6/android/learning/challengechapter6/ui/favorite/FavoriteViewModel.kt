package rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beran.common.Resource
import com.beran.domain.usecase.movie.GetAllFavoriteMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.state.ListMovieState
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val getFavoriteMovies: GetAllFavoriteMoviesUseCase) :
    ViewModel() {

    private var _favoriteMovies = MutableLiveData<ListMovieState>()
    val favoriteMovies: LiveData<ListMovieState> get() = _favoriteMovies

    init {
        getAllMovies()
    }

    private fun getAllMovies() {
        getFavoriteMovies.invoke().onEach { result ->
            when (result) {
                is Resource.Loading -> _favoriteMovies.value = ListMovieState.Loading
                is Resource.Error -> _favoriteMovies.value =
                    ListMovieState.Error(result.error)

                is Resource.Success -> _favoriteMovies.value =
                    ListMovieState.Success(result.data)
            }
        }.launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}