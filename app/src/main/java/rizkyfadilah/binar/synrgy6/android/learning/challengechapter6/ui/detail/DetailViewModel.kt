package rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beran.common.Resource
import com.beran.domain.usecase.movie.DetailMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.state.MovieState
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val detailMovieUseCase: DetailMovieUseCase) :
    ViewModel() {

    private var _detailState = MutableLiveData<MovieState>()
    val detailState: LiveData<MovieState> get() = _detailState

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

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}