package rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.beran.common.Constants
import dagger.hilt.android.AndroidEntryPoint
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.databinding.FragmentFavoriteBinding
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.MovieAdapter
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.state.ListMovieState

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<FavoriteViewModel>()
    private val movieAdapter: MovieAdapter by lazy { MovieAdapter(type = Constants.POP_MOVIE_TYPE) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFavorite.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = movieAdapter
            setHasFixedSize(true)
        }

        viewModel.favoriteMovies.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ListMovieState.Loading -> {}
                is ListMovieState.Error -> {}

                is ListMovieState.Success -> {
                    movieAdapter.submitList(result.movies)
                }
            }
        }
        movieAdapter.onclick = { item ->
            val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(item.id)
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}