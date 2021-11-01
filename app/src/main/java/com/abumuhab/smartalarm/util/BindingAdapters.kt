package com.abumuhab.smartalarm.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("imageTint")
fun imageTint(imageView: ImageView, color: Int) {
    imageView.setColorFilter(color)
}