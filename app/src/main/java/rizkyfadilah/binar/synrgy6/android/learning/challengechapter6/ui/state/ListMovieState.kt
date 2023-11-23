package rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.state

import com.beran.domain.model.MovieItem

sealed interface ListMovieState {
    data object Loading : ListMovieState
    data class Success(val movies: List<MovieItem>) : ListMovieState
    data class Error(val error: String?) : ListMovieState
}