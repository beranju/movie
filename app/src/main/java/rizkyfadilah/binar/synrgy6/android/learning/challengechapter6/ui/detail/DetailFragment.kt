package rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.beran.common.Constants.IMG_BASE_URL
import dagger.hilt.android.AndroidEntryPoint
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.R
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.databinding.FragmentDetailBinding
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui.state.MovieState
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.utils.loadUrl

/**
 * TODO
 * add bookmark icon beside the button visit to save the movie
 */

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val args: DetailFragmentArgs by navArgs()
    private val viewModel by viewModels<DetailViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id: Int = args.id
        viewModel.getDetailMovie(id)


        viewModel.detailState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MovieState.Loading -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.load_content),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is MovieState.Error -> {
                    findNavController().navigateUp()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error_message),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is MovieState.Success -> {
                    val data = state.movie
                    binding.apply {
                        val rating = data.voteAverage.toString().toFloatOrNull()?.div(2)
                        tvTitle.text = data.title
                        tvOverviewContent.text = data.overview
                        tvGenreContent.text =
                            data.genres.replace("[", "").replace("]", "")
                        rbVote.rating = rating ?: 0f
                        tvRate.text = String.format("%.1f", rating)
                        sivThumbnail.loadUrl(IMG_BASE_URL+data.posterPath.orEmpty())
                        btnSite.setOnClickListener {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(data.homepage)))
                        }
                    }
                }

                else -> {}
            }
        }
        binding.ivNavigateBack.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val id= args.id
        viewModel.getDetailMovie(id)
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}