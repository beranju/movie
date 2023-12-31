package rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.beran.common.Constants.POP_MOVIE_TYPE
import dagger.hilt.android.AndroidEntryPoint
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.R
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.databinding.FragmentHomeBinding
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.state.ListMovieState
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.MovieAdapter
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.state.UserState
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.utils.loadUrl
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.utils.showAlert
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.utils.showToast

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val nowMovieAdapter: MovieAdapter by lazy { MovieAdapter() }
    private val popMovieAdapter: MovieAdapter by lazy { MovieAdapter(type = POP_MOVIE_TYPE) }
    private val viewModel: HomeViewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvMovie.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = popMovieAdapter
        }

        binding.rvMovieNow.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = nowMovieAdapter
        }

        viewModel.userState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UserState.Loading -> {
                    showLoading(true)
                }

                is UserState.Error -> {
                    showLoading(false)
                }

                is UserState.Success -> {
                    val username = state.userData.username
                    binding.tvUsername.text = getString(R.string.greeting, username)
                    binding.sivAvatar.loadUrl(state.userData.imageUrl)
                    showLoading(false)
                    if (username.isBlank()){
//                        requireContext().showAlert("Set Profile", "Please set your profile", positiveCallback = {
//                            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
//                            it.dismiss()
//                            it.cancel()
//                        })
                    }
                }
            }
        }
        viewModel.nowPlayingMovieState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ListMovieState.Loading -> showLoading(true)
                is ListMovieState.Error -> {
                    val error = state.error
                    binding.llErrorNow.visibility = if (error != null) View.VISIBLE else View.GONE
                    showLoading(false)
                }

                is ListMovieState.Success -> {
                    val movies = state.movies
                    nowMovieAdapter.submitList(movies)
                    showLoading(false)
                }
            }
        }
        viewModel.popMovieState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ListMovieState.Loading -> showLoading(true)
                is ListMovieState.Error -> {
                    val error = state.error
                    binding.llErrorPop.visibility = if (error != null) View.VISIBLE else View.GONE
                    showLoading(false)
                }

                is ListMovieState.Success -> {
                    val movies = state.movies
                    popMovieAdapter.submitList(movies)
                    showLoading(false)
                }
            }
        }

        popMovieAdapter.onclick = { data ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(data.id)
            findNavController().navigate(action)
        }

        nowMovieAdapter.onclick = { data ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(data.id)
            findNavController().navigate(action)
        }

        binding.sivAvatar.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }
        binding.ivFavorite.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_favoriteFragment)
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.sflNowMovies.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.sflPopMovies.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvMovieNow.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.rvMovie.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSavedData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}