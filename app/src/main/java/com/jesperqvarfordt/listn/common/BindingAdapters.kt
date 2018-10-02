package com.jesperqvarfordt.listn.common

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

object ImageBindingAdapter {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun setImageUrl(imageView: ImageView, url: String?) {
        if (url.isNullOrEmpty() || imageView.tag == url) {
            return
        }
        val transformation = RoundedCornersTransformation(10, 0)
        Picasso.get().load(url).transform(transformation).into(imageView)
        imageView.tag = url
    }
}
