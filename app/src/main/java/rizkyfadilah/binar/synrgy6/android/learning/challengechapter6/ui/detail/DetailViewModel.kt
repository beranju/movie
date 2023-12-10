package rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beran.common.Resource
import com.beran.domain.model.MovieDetail
import com.beran.domain.usecase.movie.AddFavoriteMovieUseCase
import com.beran.domain.usecase.movie.DeleteFavoriteMovieUseCase
import com.beran.domain.usecase.movie.DetailMovieUseCase
import com.beran.domain.usecase.movie.IsFavoriteMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.state.MovieState
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val detailMovieUseCase: DetailMovieUseCase,
    private val isFavoriteMovieUseCase: IsFavoriteMovieUseCase,
    private val addFavoriteMovieUseCase: AddFavoriteMovieUseCase,
    private val deleteFavoriteMovieUseCase: DeleteFavoriteMovieUseCase
) :
    ViewModel() {

    private var _detailState = MutableLiveData<MovieState>()
    val detailState: LiveData<MovieState> get() = _detailState

    private var _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    fun getDetailMovie(id: Int) {
        detailMovieUseCase.invoke(id).onEach { result ->
            when (result) {
                is Resource.Loading -> _detailState.value = MovieState.Loading
                is Resource.Error -> _detailState.value =
                    MovieState.Error(result.error)

                is Resource.Success -> _detailState.value =
                    MovieState.Success(result.data)
            }
        }.launchIn(viewModelScope)
    }

    fun checkMovie(id: Int) {
        isFavoriteMovieUseCase.invoke(id).onEach { result ->
            when (result) {
                is Resource.Loading -> {}
                is Resource.Error -> _isFavorite.value = false
                is Resource.Success -> {
                    val data = result.data
                    _isFavorite.value = data
                }
            }
        }.launchIn(viewModelScope)
    }

    fun addFavoriteMovie(movieDetail: MovieDetail) {
        viewModelScope.launch { addFavoriteMovieUseCase.invoke(movieDetail).collect() }
        checkMovie(movieDetail.id)
    }

    fun deleteFavoriteMovie(movieDetail: MovieDetail) {
        viewModelScope.launch { deleteFavoriteMovieUseCase.invoke(movieDetail).collect() }
        checkMovie(movieDetail.id)
    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}