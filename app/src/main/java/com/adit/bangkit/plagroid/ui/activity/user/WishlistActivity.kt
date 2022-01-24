package com.adit.bangkit.plagroid.ui.activity.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adit.bangkit.plagroid.R
import com.adit.bangkit.plagroid.databinding.ActivityWishlistBinding

class WishlistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWishlistBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWishlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.Wishlist)

    }
}