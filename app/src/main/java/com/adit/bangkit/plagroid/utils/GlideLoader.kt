package com.adit.bangkit.plagroid.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.adit.bangkit.plagroid.R
import com.bumptech.glide.Glide

class GlideLoader(val context: Context) {
    fun loadUserPicture(image: Any, imageView: ImageView){
        try {
            Glide
                .with(context)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_placeholder)
                .into(imageView)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}