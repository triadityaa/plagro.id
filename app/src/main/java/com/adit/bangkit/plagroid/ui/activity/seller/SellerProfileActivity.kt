package com.adit.bangkit.plagroid.ui.activity.seller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adit.bangkit.plagroid.databinding.ActivitySellerProfileBinding
import com.adit.bangkit.plagroid.ui.activity.BaseActivity

class SellerProfileActivity : BaseActivity() {
    private lateinit var binding: ActivitySellerProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}