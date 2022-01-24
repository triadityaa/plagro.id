package com.adit.bangkit.plagroid.ui.activity.seller

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.adit.bangkit.plagroid.R
import com.adit.bangkit.plagroid.databinding.ActivitySellerBinding
import com.adit.bangkit.plagroid.model.Seller
import com.adit.bangkit.plagroid.ui.activity.BaseActivity
import com.adit.bangkit.plagroid.ui.activity.user.DashboardActivity
import com.adit.bangkit.plagroid.ui.activity.user.UserProfileActivity
import com.adit.bangkit.plagroid.utils.Constants

class SellerActivity : BaseActivity() {
    private lateinit var binding: ActivitySellerBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences(Constants.PLAGRO_PREFERENCES, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME,  "")!!
        binding.tvMain.text =
            resources.getString(R.string.hello_user) +
                    username +
                    resources.getString(R.string.welcome)
    }


}