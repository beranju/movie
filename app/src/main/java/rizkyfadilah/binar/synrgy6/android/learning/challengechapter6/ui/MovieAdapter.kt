package rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.beran.common.Constants.NOW_MOVIE_TYPE
import com.beran.domain.model.MovieItem
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.databinding.ItemMovieBinding
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.databinding.ItemMovieUpcomingBinding
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.utils.loadUrl


class MovieAdapter(
    private val type: Int = NOW_MOVIE_TYPE,
    var onclick: ((MovieItem) -> Unit)? = null
) :
    ListAdapter<MovieItem, RecyclerView.ViewHolder>(DIFF_UTIL) {


    inner class NowMovieViewHolder(private val binding: ItemMovieUpcomingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MovieItem?) {
            binding.apply {
                val rating = data?.voteAverage.toString().toFloatOrNull()?.div(2)
                tvTitle.text = data?.title
                rbVote.rating = rating ?: 0f
                ivThumbnail.loadUrl(data?.posterPath.orEmpty())
            }
        }

        init {
            binding.root.setOnClickListener {
                onclick?.invoke(getItem(adapterPosition))
            }
        }

    }

    inner class PopMovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MovieItem?) {
            binding.apply {
                val rating = data?.voteAverage.toString().toFloatOrNull()?.div(2)
                tvTitle.text = data?.title
                rbVote.rating = rating ?: 0f
                sivThumbnail.loadUrl(data?.posterPath.orEmpty())
            }
        }

        init {
            binding.root.setOnClickListener {
                onclick?.invoke(getItem(adapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (type == NOW_MOVIE_TYPE) {
            NowMovieViewHolder(
                ItemMovieUpcomingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            PopMovieViewHolder(
                ItemMovieBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        if (type == NOW_MOVIE_TYPE) {
            (holder as NowMovieViewHolder).bind(data)
        } else {
            (holder as PopMovieViewHolder).bind(data)
        }
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<MovieItem>() {
            override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean =
                oldItem.hashCode() == newItem.hashCode()
        }
    }
}