package rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.utils

import android.widget.ImageView
import com.beran.common.Constants.IMG_BASE_URL
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import rizkyfadilah.binar.synrgy6.android.learning.challengechapter6.R

fun ImageView.loadUrl(url: String) =
    Glide.with(this.context)
        .load(IMG_BASE_URL + url)
        .apply(
            RequestOptions.placeholderOf(R.drawable.ic_load_img)
                .error(R.drawable.ic_broken_img)
        )
        .into(this)