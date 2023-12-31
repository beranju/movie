package rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.state

import com.beran.domain.model.MovieDetail

sealed interface MovieState {
    data object Loading : MovieState
    data class Success(val movie: MovieDetail) : MovieState
    data class Error(val error: String?) : MovieState
}

sealed interface FavoriteState {
    data object Loading : FavoriteState
    data class Success(val isFavorite: Boolean) : FavoriteState
    data class Error(val error: String?) : FavoriteState
}